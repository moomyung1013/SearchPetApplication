import os
import numpy as np
from numpy.linalg import norm
import PIL
import time
import tensorflow as tf
from tensorflow.keras.preprocessing import image
from tensorflow.keras.preprocessing.image import ImageDataGenerator
from tensorflow.keras.applications.resnet50 import ResNet50, preprocess_input
import math
from sklearn.neighbors import NearestNeighbors
import matplotlib.pyplot as plt
import matplotlib.image as mpimg
from sklearn.decomposition import PCA

start = time.time()

f = open('data.txt','r')
data_number = int(f.read())
if data_number <= 4:
    k_number = data_number
    components = data_number
elif data_number <= 17:
    k_number = 5
    components = data_number
elif data_number == 0:
    exit()
else:
    k_number = 5
    components = 18

image_path = os.path.dirname(os.path.realpath(__file__)) + '/images'

img_size = 256 #input size
model_resnet = ResNet50(weights='imagenet', include_top=False,input_shape=(img_size, img_size, 3),pooling='max')

batch_size = 64
img_generator = ImageDataGenerator(preprocessing_function = preprocess_input)
data_generator = img_generator.flow_from_directory(image_path, target_size = (img_size,img_size), batch_size = batch_size, class_mode = None, shuffle=False)

num_images = data_generator.samples
num_epochs = int(math.ceil(num_images / batch_size))

feature_list = model_resnet.predict_generator(data_generator, num_epochs)

filenames = [image_path + '/' + s for s in data_generator.filenames]
pca = PCA(n_components = components)
pca.fit(feature_list)
compressed_features = pca.transform(feature_list)

neighbors = NearestNeighbors(n_neighbors=k_number, algorithm='ball_tree', metric='euclidean',radius = 1.6)
neighbors.fit(compressed_features)

search_file = os.path.dirname(os.path.realpath(__file__)) + '/capture_img.jpg'
input_shape = (img_size, img_size, 3)
img = image.load_img(search_file, target_size=(input_shape[0], input_shape[1]))
img_array = image.img_to_array(img)
expanded_img_array = np.expand_dims(img_array, axis=0)
preprocessed_img = preprocess_input(expanded_img_array)
test_img_features = model_resnet.predict(preprocessed_img, batch_size=1)
image_test = pca.transform(test_img_features)
distance , indices = neighbors.kneighbors(image_test)

f = open("./result.txt", 'w')

def similar_images(indices):
    number = 1    
    for index in indices:
        if number <=len(indices) :
            print(filenames[index].split("/")[6])
            f.write(filenames[index].split("/")[6]+"\n")      
            number +=1
    f.close()
print("Searct Time : ",time.time() - start)

print('Predict Result')
similar_images(indices[0])