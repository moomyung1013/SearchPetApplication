import MySQLdb
import requests
import json
import urllib
import os
import shutil
import time

while True:
    conn = MySQLdb.connect(host = '**.**.**.**', port=1111, user = 'root', password='potato', database='potato', charset='utf8') #데이터베이스 서버 연결(host와 port는 주기적으로 바뀜)
    cursor = conn.cursor()
    sql = "select name from CaptureImage"
    cursor.execute(sql)
    datas = cursor.fetchall()
    if len(datas) == 1:
        start = time.time()
        info_url = requests.get("https://potato-pizza-mytz.run.goorm.io/capture_info") #서버에서 검색 정보 데이터 받아옴
        text = info_url.text
        data =json.loads(text)
        capture_image_name = data[0]['name']
        image_url = "https://potato-pizza-mytz.run.goorm.io/capture_image/" + capture_image_name
        urllib.request.urlretrieve(image_url, capture_image_name)
        os.system('python image_download.py')
        os.system('python search.py')
        data_file = open('./data.txt','r')
        number = int(data_file.read())
        rank = 1
        val = []
        if number != 0:
            result_file = open('./result.txt', 'rb')
            
            while True:
                line = result_file.readline()
                if not line: break
                data_result = str(line).split('\\')[0][2:]
                select_sql = 'select * from CrawlData where CrawlData.img=%s union select * from RegisterData where RegisterData.img=%s'
                cursor.execute(select_sql, (data_result,data_result,))
                result = cursor.fetchall()
                for i in result:
                    list_data = list(i)
                    list_data[0] = rank
                    list_data[7] = list_data[11]
                    list_data[8] = list_data[12]
                    del list_data[9:]
                    val.append(tuple(list_data))
                rank += 1
            delete_sql = 'delete from CaptureImage'
            insert_result_sql = 'insert into ResultData values (%s, %s,%s, %s, %s, %s, %s, %s, %s)'
            cursor.execute(delete_sql)
            cursor.executemany(insert_result_sql, val)
            conn.commit()
            result_file.close()
            os.remove('./result.txt')
        else:
            delete_sql = 'delete from CaptureImage'
            cursor.execute(delete_sql)
            conn.commit()
        data_file.close()
        os.remove('./data.txt')
        print("Total Time :",time.time() - start)
    else:
        print("Waiting for image...")
        continue