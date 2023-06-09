# Menggunakan image Python versi terbaru
FROM python:latest

# Menetapkan direktori kerja di dalam container
WORKDIR /app

# Copy file requirements.txt ke dalam container
COPY requirements.txt .

#working directory = dalam container
# Copy file ServiceAccountKey ke dalam working directory
COPY ServiceAccountKey.json /app/ServiceAccountKey.json

# Menyalin direktori API dan ML ke dalam working directory
COPY API /app/API
COPY ML /app/ML

# Menginstal dependensi Python melalui file requirements.txt
RUN pip install --no-cache-dir -r requirements.txt

# Menjalankan perintah untuk run server saat container dijalankan
CMD [ "python", "API/main.py" ]
