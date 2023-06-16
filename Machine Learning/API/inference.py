import matplotlib
import tensorflow as tf
import numpy as np
import os
import cv2
import matplotlib.pyplot as plt
import matplotlib.image as mpimg
from tensorflow.lite.python.interpreter import Interpreter
from PIL import Image

print(tf. __version__)
# https: // stackoverflow.com/questions/53684971/assertion-failed-flask-server-stops-after-script-is-run
matplotlib.use('Agg')

def get_category(img):
    model_tflite = 'models/detect.tflite'
    
    #Load Model TFLite
    Interpreter = tf.lite.Interpreter(model_path=model_tflite)
    Interpreter.allocate_tensors()
    
    #Get input and output details from models (Mengambil data dari model TFLite)
    input_details = Interpreter.get_input_details()
    output_details = Interpreter.get_output_details()

    #Load and Process the input image 
    image = Image.open(img) #Masukkan images
    image = image.resize((input_details[0]['shape'][1], input_details[0]['shape'][2])) #Ubah ukuran image sesuai model
    input_data = np.expand_dims(image, axis=0) #Menambahkan dimensi input tambahan sesuai dengan model
    input_data = (np.float32(input_data) - 127.5) / 127.5 #Normalisasi pada gambar 

    #Set the input tensor
    Interpreter.set_tensor(input_details[0]['index'], input_data) #Mengatur nilai tensor inputan dari data input_data

    #Run the inference 
    Interpreter.invoke()

    #Get the output tensor 
    output_data = Interpreter.get_tensor(output_details[0]['index'])[0]
    output_classes = Interpreter.get_tensor(output_details[3]['index'])[0]
    output_scores = Interpreter.get_tensor(output_details[0]['index'])[0]

    # Set a threshold for minimum confidence score
    confidence_threshold = 0.5

    #Open label Object Detection (Labelmap.txt)
    labels = 'labels/labelmap.txt'
    with open(labels, "r") as f:
        labelmap = f.read().splitlines()

    detections = []

    for i in range(len(output_data)):
        if ((output_data[i] > confidence_threshold) and (output_data[i] <= 1.0)):
            object_name = labelmap[int(output_classes[i])]
            detections.append(object_name)

    result = detections
    return result

