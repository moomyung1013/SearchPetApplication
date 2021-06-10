import requests
import json
import urllib
import os
import shutil
import time
start = time.time()
my_path = os.path.dirname(os.path.realpath(__file__)) + '/images/pet_image/'

info_url = requests.get("https://potato-pizza-mytz.run.goorm.io/search_info")

text = info_url.text
data =json.loads(text)
print(len(data))

file = open('data.txt','w') #data count
file.write(str(len(data)))
file.close()

if os.path.exists(my_path):
    shutil.rmtree("./images/pet_image")

if not os.path.exists(my_path):
    os.makedirs(my_path)


for i in range(len(data)):
    image_url = "https://potato-pizza-mytz.run.goorm.io/image/" + data[i]['img']
    image_path = my_path+data[i]['img']
    urllib.request.urlretrieve(image_url, image_path)

print(time.time() - start)

