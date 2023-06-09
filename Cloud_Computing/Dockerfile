# Menggunakan image Python versi terbaru
FROM python:latest

# Menetapkan direktori kerja di dalam container
WORKDIR /app

# Menyalin file requirements.txt ke dalam container
COPY requirements.txt .
COPY ServiceAccountKey.json /app/ServiceAccountKey.json

# Menyalin direktori API ke dalam container
COPY API /app/API

# Menyalin direktori ML ke dalam container
COPY ML /app/ML

# Menginstal dependensi Python
RUN pip install --no-cache-dir -r requirements.txt

# Menjalankan perintah saat container dijalankan
CMD [ "python", "API/main.py" ]
