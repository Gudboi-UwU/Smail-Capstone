from flask import Flask, request, render_template
from inference import get_category
from datetime import datetime
import time

app = Flask(__name__)

@app.route('/', methods=['GET', 'POST'])
def object_detection():
    if request.method == 'POST':
    # POST method to post the results file
        # Read file from upload
        img = request.files['file']
        #Start Time
        start_time = time.time()
        # Get category of prediction
        image_category = get_category(img)
        #End Time
        end_time = time.time()
        # Calculate the processing time in milliseconds
        processing_time = (end_time - start_time)
        print("Processing time:", processing_time, "seconds")
        print("Processing time:", processing_time * 1000, "miliseconds")
        # Plot the category
        #now = datetime.now()
        #current_time = now.strftime("%H-%M-%S")
        # Render the result template
        return render_template('result.html', category=image_category)
    # For GET requests, load the index file
    return render_template('index.html')

if __name__ == '__main__':
    app.run(debug=True)
