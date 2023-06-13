# Main.py merupakan file gabungan antara API CC dan API ML

#import library
from flask import Flask, request, jsonify, render_template #tambahan ml render
import pymysql
import os
import dotenv
import firebase_admin
from firebase_admin import credentials, auth
import requests
from flask_mail import Mail, Message
from datetime import datetime #tambahan ml

#tambahan ml
import matplotlib
import tensorflow as tf
import numpy as np
import matplotlib.pyplot as plt
import matplotlib.image as mpimg
from tensorflow.lite.python.interpreter import Interpreter #tflite
from PIL import Image

app = Flask(__name__)


# Konfigurasi koneksi ke Cloud SQL
db_user = os.environ.get('CLOUD_SQL_USERNAME')
db_password = os.environ.get('CLOUD_SQL_PASSWORD')
db_name = os.environ.get('CLOUD_SQL_DATABASE_NAME')
db_connection_name = os.environ.get('CLOUD_SQL_CONNECTION_NAME')

# Konfigurasi Firebase
main_dir = os.path.dirname(os.path.abspath(__file__))
service_account_dir = os.path.join(main_dir, '..')
service_account_path = os.path.join(service_account_dir, 'ServiceAccountKey.json')
cred = credentials.Certificate(service_account_path)
# cred = credentials.Certificate("ServiceAccountKey.json")

firebase_admin.initialize_app(cred)



# #Konfigurasi email
# app.config['MAIL_SERVER'] = 'smtp.gmail.com'
# app.config['MAIL_PORT'] = 587
# app.config['MAIL_USERNAME'] = os.getenv("env_email")
# app.config['MAIL_PASSWORD'] = os.getenv("env_password")
# app.config['MAIL_USE_TLS'] = True
# app.config['MAIL_USE_SSL'] = False

# mail = Mail(app)



#Endpoint untuk mengetes API
@app.route('/', methods = ['GET'])
def tes():
    return("Api telah berjalan dengan baik !")


# Endpoint /register untuk melakukan registrasi user ke Firebase
@app.route('/register', methods=['POST'])
def firebase_register():
    email = request.json['email']
    password = request.json['password']
    try:
        # Membuat user baru kedalam firebase
        user = auth.create_user(
            email=email,
            password=password
        )
        return jsonify({'message': 'Registrasi Anda telah berhasil', 'UserID': user.uid})

    except Exception as e:
        return jsonify({'error': 'Registrasi Anda gagal', 'error': str(e)}), 400


# Endpoint /login untuk melakukan login user
@app.route('/login', methods=['POST'])
def firebase_login():
    email = request.json['email']
    password = request.json['password']
    try:
        user = auth.get_user_by_email(email)
        if user:
            dotenv.load_dotenv()
            firebase_api_key = os.getenv("env_firebase_web_api_key")
            login_url = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key={}".format(firebase_api_key)
            json_request = {"email": email, "password": password}
            login_firebase = requests.post(login_url, json=json_request)
            return jsonify({'message': 'Anda berhasil login, Selamat datang !', "data": login_firebase.json()})
        else:
            return jsonify({'error': 'Anda gagal melakukan login', 'error': 'User tidak ditemukan'}), 401

    except Exception as e:
        return jsonify({'error': 'Anda gagal melakukan login', 'error': str(e)}), 401



# Fungsi untuk mengirim link reset password
# Di nonaktifkan untuk beberapa alasan
# @app.route('/reset-password', methods=['POST'])
# def firebase_reset_password():
#     email = request.json['email']

#     try:
#         # Mengirim link reset password ke email user
#         reset_link = auth.generate_password_reset_link(email)

#         # Mengirim email reset password
#         msg = Message('Smail Reset Password', sender=app.config['MAIL_USERNAME'], recipients=[email])
#         msg.body = 'Klik link berikut untuk mereset password Anda: {}'.format(reset_link)
#         mail.send(msg)

#         return jsonify({'message': 'Link reset password telah dikirim ke email Anda' ,'link':reset_link})

#     except Exception as e:
#         return jsonify({'message': 'Gagal mengirim link reset password', 'link':reset_link , 'error': str(e)}), 400



# Fungsi untuk melakukan koneksi terhadap Cloud SQL
def make_connection():

    if os.environ.get('GAE_ENV') == 'standard':
        
        unix_socket = '/cloudsql/{}'.format(db_connection_name)
        cnx = pymysql.connect(user=db_user, password=db_password,
                              unix_socket=unix_socket, db=db_name)
    else:

        host = '34.128.82.5'
        cnx = pymysql.connect(user=db_user, password=db_password,
                              host=host, db=db_name)

    return cnx


# Endpoint /barang untuk mendapatkan semua data barang
@app.route('/barang', methods=['GET'])
def get_all_barang():
    try:
        cnx = make_connection()
        cursor = cnx.cursor()
        sql = "SELECT * FROM data_barang"
        cursor.execute(sql)

        data_barang = cursor.fetchall()

        cursor.close()
        cnx.close()

        data_barang = []
        for row in data_barang:
            barang = {
                'id': row[0],
                'nama_barang': row[1],
                'harga': row[2]
            }
            data_barang.append(barang)


        return jsonify(data_barang)

    except pymysql.Error as e:
        return jsonify({'error': 'Gagal mendapatkan barang', 'message': str(e)}), 500

    

# Endopint /barang/id_barang untuk mendapatkan salah satu barang dari Cloud SQL
@app.route('/barang/<int:id>', methods=['GET'])
def get_barang_by_id(id):
    try:
        cnx = make_connection()
        cursor = cnx.cursor()
        sql = f"SELECT * FROM data_barang WHERE id = {id}"
        cursor.execute(sql)

        barang = cursor.fetchone()

        cursor.close()
        cnx.close()

        if barang:
            return jsonify(barang)
        else:
            return jsonify({'message': 'Barang tidak ditemukan'}), 404

    except pymysql.Error as e:
        return jsonify({'error': 'Gagal mendapatkan barang', 'message': str(e)}), 500
    


# Endopint /tambah_penjualan untuk menambahkan penjualan dan detail penjualan
@app.route('/tambah_penjualan', methods=['POST'])
def tambah_penjualan():
    try:
        data = request.get_json()
        tanggal = data['tanggal']
        total_keseluruhan = data['total_keseluruhan']
        daftar_barang = data['daftar_barang']

        cnx = make_connection()
        cursor = cnx.cursor()
     
        sql = "INSERT INTO penjualan (tanggal, total_keseluruhan) VALUES (%s, %s)"
        cursor.execute(sql, (tanggal, total_keseluruhan))
        id_penjualan = cursor.lastrowid
        
        for barang in daftar_barang:
            id_barang = barang['id_barang']
            jumlah = barang['jumlah']
            total_barang = barang['total_barang']

            insert_detail_penjualan_query = "INSERT INTO detail_penjualan (id_penjualan, id_barang, jumlah, total_barang) VALUES (%s, %s, %s, %s)"
            cursor.execute(insert_detail_penjualan_query, (id_penjualan, id_barang, jumlah, total_barang))
        
        cnx.commit()
        cursor.close()
        cnx.close()

        return jsonify({'message': 'Penjualan berhasil ditambahkan'})

    except Exception as e:
        return jsonify({'error': 'Terjadi kesalahan, penjualan gagal ditambahkan', 'message': str(e)}), 500



# Endpoint /penjualan untuk mendapatkan semua data dari tabel penjualan
@app.route('/penjualan', methods=['GET'])
def get_all_penjualan():
    try:  
        cnx = make_connection()
        cursor = cnx.cursor()

        sql = "SELECT * FROM penjualan"
        cursor.execute(sql)
        penjualan_data = cursor.fetchall()

        data_penjualan = []

        for penjualan in penjualan_data:
            id_penjualan = penjualan[0]
            tanggal = penjualan[1]
            total_keseluruhan = penjualan[2]

            data = {
                'id_penjualan': id_penjualan,
                'tanggal': tanggal,
                'total_keseluruhan': total_keseluruhan
            }   
            data_penjualan.append(data)

        cursor.close()
        cnx.close()

        return jsonify(data_penjualan)

    except Exception as e:
        return jsonify({'error': 'Gagal mendapatkan data penjualan', 'message': str(e)}), 500


# Endpoint untuk mendapatkan data dari tabel detail_penjualan berdasarkan ID penjualan
@app.route('/detail_penjualan/<int:id_penjualan>', methods=['GET'])
def get_detail_penjualan_by_id(id_penjualan):
    try:
        cnx = make_connection()
        cursor = cnx.cursor()

        sql = "SELECT * FROM detail_penjualan WHERE id_penjualan = %s"
        cursor.execute(sql, (id_penjualan,))
        detail_penjualan_data = cursor.fetchall()

        data_detail_penjualan = []

        for detail_penjualan in detail_penjualan_data:
            id_detail = detail_penjualan[0]
            id_barang = detail_penjualan[2]
            jumlah = detail_penjualan[3]
            total_barang = detail_penjualan[4]

            data = {
                'id_detail': id_detail,
                'id_barang': id_barang,
                'jumlah': jumlah,
                'total_barang': total_barang
            }
            data_detail_penjualan.append(data)

        cursor.close()
        cnx.close()

        return jsonify(data_detail_penjualan)

    except Exception as e:
        return jsonify({'error': 'Gagal mendapatkan detail penjualan', 'message': str(e)}), 500



# Bagian Machine Learning untuk prediksi
# Dibuat oleh : Ammar Khaq Baasir

matplotlib.use('Agg')


#Endpoint /detect untuk melakukan deteksi barang
@app.route('/detect', methods=['GET', 'POST'])
def object_detection():
    if request.method == 'POST':
        if 'file' not in request.files:
            return jsonify({'error': 'No file uploaded'}), 400
        
        # Read file from upload
        img = request.files['file']
        
        current_dir = os.path.dirname(os.path.abspath(__file__))
        print (current_dir)
        main_dir = os.path.join(current_dir,'..')
        model_dir = os.path.join(main_dir, 'ML', 'models')

        print (model_dir)
        model_tflite = os.path.join(model_dir, 'detect.tflite')
        print (model_tflite)
        
        # Load Model TFLite
        Interpreter = tf.lite.Interpreter(model_path=model_tflite)
        Interpreter.allocate_tensors()
        
        # Get input and output details from models
        input_details = Interpreter.get_input_details()
        
        # Load and Process the input image 
        image = Image.open(img)
        image = image.resize((input_details[0]['shape'][1], input_details[0]['shape'][2]))
        input_data = np.expand_dims(image, axis=0)
        input_data = (np.float32(input_data) - 127.5) / 127.5
        
        # Set the input tensor
        Interpreter.set_tensor(input_details[0]['index'], input_data)
        
        # Run the inference 
        Interpreter.invoke()
        
        # Get the output tensor 
        output_details = Interpreter.get_output_details()
        output_data = Interpreter.get_tensor(output_details[0]['index'])[0]
        output_classes = Interpreter.get_tensor(output_details[3]['index'])[0]
        output_scores = Interpreter.get_tensor(output_details[0]['index'])[0]
        
        # Set a threshold for minimum confidence score
        confidence_threshold = 0.5
        
        # Open label Object Detection (Labelmap.txt)
        # labels = 'labelmap.txt'
        # with open(labels, "r") as f:
        #     labelmap = f.read().splitlines()

        current_dir = os.path.dirname(os.path.abspath(__file__))
        main_dir = os.path.join(current_dir, '..')
        label_dir = os.path.join(main_dir, 'ML', 'labels')
        labels = os.path.join(label_dir, 'labelmap.txt')

        with open(labels, "r") as f:
            labelmap = f.read().splitlines()
        
        detections = []
        
        for i in range(len(output_data)):
            if ((output_data[i] > confidence_threshold) and (output_data[i] <= 1.0)):
                object_name = labelmap[int(output_classes[i])]
                detections.append(object_name)
        
        image_category = detections
        now = datetime.now()
        current_time = now.strftime("%H-%M-%S")
        
        # Return the result as JSON
        return jsonify({
            'category': image_category,
            'current_time': current_time
        })
    
    # For GET requests
    return ('Anda menggunakan method yang salah')


# Menjalankan server
if __name__=="__main__":
    app.run(debug=True, host='0.0.0.0', port='8045')

