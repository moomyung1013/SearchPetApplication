import MySQLdb
import requests
from bs4 import BeautifulSoup
from urllib.request import urlopen
from urllib.parse import quote_plus

#Database에 이미지 정보 업데이트
def updateImgToDB(min, max):
    for i in range(min, max):
        sql = "UPDATE CrawlData SET img=%s WHERE id=%s"
        val = ("img"+str(i)+".jpg", i)
        cursor.execute(sql, val)
        conn.commit()    
    conn.close()

    
def crawl(start, end, N):
    n = N
    for i in range(start, end):
        url_ = url + str(i)
        webpage = requests.get(url_)
        soup = BeautifulSoup(webpage.content, "html.parser")
        dataList = soup.find_all('tr')

        imgList = soup.find("ul", class_ ="publicPhotoArea")

        print(url_)
        if dataList[0].find_all('td')[0].string==None:
            print("pass!")
            continue
        crawlList = []
        for idx, val in enumerate(dataList):
            data1 = val.find_all('td')
            for d in data1:
                if idx==0 or idx==1 or idx==3 or idx==4 or idx==6 or idx==7 or idx==11 or idx==12:
                    if idx==0:
                        dd = d.string
                        text1 = dd.split('-')
                        crawlList.append(text1[0])
                        crawlList.append(text1[1])
                    elif idx==1: 
                        dd = d.string
                        text1 = dd.replace("\n","")
                        text2 = text1.replace(" ","")
                        text3 = text2.split('[')[1]
                        text4 = text3.split(']')
                        crawlList.append(text4[0])
                        crawlList.append(text4[1].split('\r')[0])
                    elif idx==4:
                        dd = d.string
                        text1 = dd.split('/')[0].replace(" ", "")
                        crawlList.append(text1.split('\xa0')[0])
                    else:
                        if "\xa0" in d.string:
                            crawlList.append(d.string.split('\xa0')[0])
                        else:
                            crawlList.append(d.string)
        kind = "강아지" if crawlList[2]=="개" else "고양이"
        sql = "INSERT INTO CrawlData VALUES(NULL, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, NULL)"
        val = (crawlList[6], kind, crawlList[3], crawlList[4], crawlList[0], crawlList[1], crawlList[10], crawlList[8], crawlList[5], crawlList[7], crawlList[9])
        cursor.execute(sql, val)
        conn.commit()

        imgUrl = "https://www.animal.go.kr/" + imgList.find('img')['src']
        imgname = 'img' + str(n) + '.jpg'
        print(imgname)
        with urlopen(imgUrl) as f:
            with open('./images/'+imgname, 'wb') as h:
                i = f.read()
                h.write(i)
        n+=1
    conn.close()
        
    
conn = MySQLdb.connect(
    user="root",
    passwd="",
    host="localhost",
    db="potato",
    charset="utf8",
    use_unicode=True
)
cursor = conn.cursor()
cursor.execute("set names utf8")
totalCount=5653&pageSize=10&menuNo=1000000060&&page="
url = "https://www.animal.go.kr/front/awtis/protection/protectionDtl.do?desertionNo="

'''
startNum: 크롤링을 수행할 첫 페이지의 num
endNum: 크롤링을 수행할 마지막 페이지의 num
startIdx: DB 테이블에 추가할 데이터의 첫번째 인덱스
endIdx: DB 테이블에 추가할 데이터의 마지막 인덱스
N: 인덱스
'''

startNum, endNum , startIdx, endIdx, N= _, _, _, _, _
crawlstartNum, endNum, N)
updateImgToDB(startIdx, endIdx)

