const express = require('express');
const mysql = require('mysql');
const app = express();
var multer = require('multer');
var upload = multer({
    dest: './capture_images'
});
var buffer = require('buffer');
var path = require('path');
var fs = require('fs');


app.listen('80', () => {
    console.log('Server Started');
});
const dbc = mysql.createConnection({
    host   :'localhost',
   user   :'root',
   password   :'',
   database   :'potato',
   multipleStatements: true
});

dbc.connect();

app.get('/', (req, res) => { //크롤링, 등록 데이터 최신 날짜순으로 정렬
   dbc.query('select * from CrawlData union select * from RegisterData order by date desc',(error, rows, fields) => {
        if (error) throw error;
        res.send(rows);
   });
}); 

app.get('/resut', (req, res) => {
   dbc.query('SELECT * FROM CrawlData',(error, rows, fields) => {
        if (error) throw error;
        res.send(rows);
   });
});

app.get('/image_query', (req, res) => {
    var query = `SELECT img FROM CrawlData WHERE id=${req.query.id}`;
    dbc.query(query, (err, result, fields) => {
        if (err) throw err;

        if (result[0]) {
            //console.log(__dirname + "images/" + result[0].image);
            res.sendFile(__dirname + "/images/" + result[0].image);
        }
        else
            res.send(404, "Not Found");
    });
});



app.use('/image', express.static('images'));
app.use('/find_result/image', express.static('images'));
app.use('/adopt_result/image', express.static('images'));


app.get('/search_info', (req,res) => {
   dbc.query('select id, date, CrawlData.kind, variety, gender , CrawlData.place, CrawlData.place2, phone, img from CrawlData, CaptureImage where CaptureImage.place = CrawlData.place and CaptureImage.place2 = CrawlData.place2 and CaptureImage.kind = CrawlData.kind UNION select id, date, RegisterData.kind, variety, gender , RegisterData.place, RegisterData.place2, phone, img from RegisterData, CaptureImage where CaptureImage.place = RegisterData.place and CaptureImage.place2 = RegisterData.place2 and CaptureImage.kind = RegisterData.kind', (error, rows, fields) => {
      if (error) throw error;
      res.send(rows);
   });
});



app.get('/capture_info', (req, res) => {
   dbc.query('SELECT * FROM CaptureImage',(error, rows, fields) => {
        if (error) throw error;
        res.send(rows);
   });
});

app.get('/capture_image', (req, res) => {
    var query = `SELECT capture_image FROM CaptureImage`;
    dbc.query(query, (err, result, fields) => {
        if (err) throw err;

        if (result[0]) {
            res.sendFile(__dirname + "/capture_images/" + result[0].image);
        }
        else
            res.send(404, "Not Found");
    });
});


app.use('/capture_image', express.static('capture_images'));

app.get('/find_result', (req, res) => {
   dbc.query('SELECT * FROM ResultData',(error, rows, fields) => {
        if (error) throw error;
        res.send(rows);
      res.sendFile(__dirname + "/images/" + rows[0].image);
   });
   
});


app.use('/write_result/image', express.static('write_images'));

app.get('/write_result', (req, res) => {
   dbc.query('SELECT * FROM WriteData',(error, rows, fields) => {
        if (error) throw error;
        res.send(rows);
   });
   
});


app.post('/search', (req, res) => {
   console.log('who get in here post /users');
   var paramDecoded;
   var inputData;
   req.on('data', (data) => {
      inputData = data
      paramDecoded = decodeURIComponent(inputData);  // <- 텍스트 파싱때 필요, 이미지랑은 관련 X
      
   });

   req.on('end', () => {
      var array_field= [];
      var array_name = [];
      var test = paramDecoded.split('&');
     //텍스트 끊는 코드
      for ( var i in test) {
         if(test[i]=='')
            break;
         var test1 = test[i].split('=')[0];
         var test2 = test[i].split('=')[1];
         if(test1 == 'name') {
            var bufwrite = test2
            test2 = 'capture_img.jpg'
         }
         if (test1 == 'place2'){
            if (test2 == '없음')
               test2 = '세종'
         }
         array_field.push(test1)
         array_name.push(test2)
      }
       var buf = Buffer.from(bufwrite,'base64');
       fs.writeFileSync("/workspace/Potato_Pizza/dalbok/capture_images/capture_img.jpg", buf);  
       console.log('******** base64로 인코딩되었던 파일 쓰기 성공 ********');
      
         dbc.query('delete from ResultData',(error, rows, fields) => {
        if (error) throw error;
       });
      
      var sql = 'insert into CaptureImage ('+array_field[0]+','+array_field[1]+','+array_field[2]+','+array_field[3]+') values (?, ?, ?, ?)';
      dbc.query(sql, [array_name[0],array_name[1],array_name[2],array_name[3]], function(err,result,field){
         if(err){
            console.log(err);
         }
      });
      
   });

   res.write("OK!");
   res.end();
});
var queryTxt;
var qeuryTxt1;
var queryTxt2;
var queryTxt3;
var queryTxt4;

var array_field= [];
var array_name = [];

app.post('/adopt', (req, res) => {
   console.log('who get in here post /users');
   var paramDecoded;
   var inputData;
   req.on('data', (data) => {
      inputData = data
      paramDecoded = decodeURIComponent(inputData);  // <- 텍스트 파싱때 필요, 이미지랑은 관련 X
      
   });

   req.on('end', () => {
        array_field= [];
        array_name = [];
       var test = paramDecoded.split('&');
      //텍스트 끊는 코드
      for ( var i in test) {
         if(test[i]=='')
            break;
         var test1 = test[i].split('=')[0];
         var test2 = test[i].split('=')[1];
         
         array_field.push(test1)
         array_name.push(test2)
        }
      console.log(array_field);
      console.log(array_name);
      
      queryTxt1 = 'select * from CrawlData where '+array_field[0]+'="'+array_name[0]+'" and '+array_field[1] +'="'+array_name[1]+'" and '+array_field[2]+'="'+array_name[2]+'" and '+array_field[3]+'="'+array_name[3]+'" LIMIT 10;' //모든 조건
      queryTxt2 = 'select * from CrawlData where '+array_field[1]+'="'+array_name[1]+'" and '+array_field[2]+'="'+array_name[2]+'" and '+array_field[3]+'="'+array_name[3]+'" LIMIT 10;' //성별 제외 조건
      queryTxt3 = 'select * from CrawlData where '+array_field[2]+'="'+array_name[2]+'" and '+array_field[3]+'="'+array_name[3]+'" LIMIT 10;'
            //품종 제외 조건
      queryTxt4 = 'select * from CrawlData where '+array_field[2]+'="'+array_name[2]+'" LIMIT 10;'
            //지역 제외 조건
      query_length();
   });

   res.write("OK!");
   res.end();
});

function query_length() {
   query_total = queryTxt1+queryTxt2+queryTxt3+queryTxt4;
   console.log(query_total);
   dbc.query(query_total, function(error, rows, fields) {
      if (error) throw error;
      if (rows[0].length != 0)
         queryTxt = queryTxt1;
      else if (rows[1].length != 0)
         queryTxt = queryTxt2;
      else if (rows[2].length != 0)
         queryTxt = queryTxt3;
      else if (rows[3].length != 0)
         queryTxt = queryTxt4;
   });
   
}

app.get('/adopt_result', (req, res) => {
   query_2 = queryTxt
   dbc.query(query_2,(error, rows, fields) => {
      if (error) throw error;
      console.log(rows);
      res.send(rows);
      res.sendFile(__dirname + "/images/" + rows[0].image);
   });
});
app.post('/register', (req, res) => {
   console.log('who get in here post /users');
   var paramDecoded;
   var inputData;
   var array_field= [];
    var array_name = [];
   var id_num;
   var img_file_num;
   var bufwrite;
   var phone_num;
   var phone_false = 0;
   var dir = './images';
   
   req.on('data', (data) => {
      inputData = data
      paramDecoded = decodeURIComponent(inputData);  // <- 텍스트 파싱때 필요, 이미지랑은 관련 X
      
   });

   req.on('end', () => {
      var test = paramDecoded.split('&');
     //텍스트 끊는 코드
      for ( var i in test ) {
         if(test[i]=='')
            break;
         test1 = test[i].split('=')[0]; //field
         test2 = test[i].split('=')[1]; //name
         if (test1 == 'img'){
            bufwrite = test2;
            img_file_num = i;
            test2 = 'register_img.jpg';
         }
         if (test1 == 'safe'){
            if (test2 == '아니오')
               phone_false = 1;
            continue;
         }
         if (test1 == 'variety'){
            if(test2 == '몰라요')
               test2 = '미상';
         }
         if (test1 == 'gender'){
            if(test2 == '몰라요')
               test2 = '미상';
         }
         if (test1 == 'phone')
            phone_num = i;
         if(test1 == 'place2'){
            if (test2 == '없음')
               test2 = '세종';
         }
         
         array_field.push(test1);
         array_name.push(test2);
      }
      
   });
   
   fs.readdir(dir, (err, files) => {
       id_num = String(files.length + 1);
       array_name[img_file_num] = 'register_img'+id_num+'.jpg';
       var buf = Buffer.from(bufwrite,'base64');

       fs.writeFileSync("./images/"+array_name[img_file_num], buf);
      console.log('******** base64로 인코딩되었던 파일 쓰기 성공 ********');
      if (phone_false == 1)
         array_name[phone_num] = null;
      let today = new Date();
      let year = today.getFullYear();
      let month = today.getMonth() + 1;
      let date = today.getDate();
      var day_string = year+"-"+month+"-"+date;
      array_field.push("date");
      array_name.push(day_string);
      var sql = 'insert into RegisterData ('+array_field[0]+','+array_field[1]+','+array_field[2]+','+array_field[3]+','+array_field[4]+','+array_field[5]+','+array_field[6]+','+array_field[7]+') values (?, ?, ?, ?, ?, ?, ?, ?)';
      dbc.query(sql, [array_name[0],array_name[1],array_name[2],array_name[3],array_name[4],array_name[5],array_name[6],array_name[7]], function(err,result,field){
         if(err){
            console.log(err);
         }
      });
   });

   res.write("OK!");
   res.end();
});



app.post('/write', (req, res) => {
   console.log('who get in here post /users');
   var paramDecoded;
   var inputData;
   var array_field= [];
    var array_name = [];
   var id_num;
   var img_file_num;
   var bufwrite;
   var dir = './write_images';
   
   req.on('data', (data) => {
      inputData = data
      paramDecoded = decodeURIComponent(inputData);
      
   });

   req.on('end', () => {
      var test = paramDecoded.split('&');
     //텍스트 끊는 코드
      for ( var i in test ) {
         if(test[i]=='')
            break;
         test1 = test[i].split('=')[0]; //field
         test2 = test[i].split('=')[1]; //name
         if (test1 == 'img'){
            bufwrite = test2;
            img_file_num = i;
            test2 = 'write_img.jpg';
         }
         if (test1 == 'content'){
            test2 = test2.replace(/\+/g, ' ');
         }
         
         array_field.push(test1);
         array_name.push(test2);
      }
      
   });
   
   fs.readdir(dir, (err, files) => {
       id_num = String(files.length + 1);
       array_name[img_file_num] = 'write_img'+id_num+'.jpg';
       var buf = Buffer.from(bufwrite,'base64');

       fs.writeFileSync("./write_images/"+array_name[img_file_num], buf);
      console.log('******** base64로 인코딩되었던 파일 쓰기 성공 ********');
      
      let today = new Date();
      let year = today.getFullYear();
      let month = today.getMonth() + 1;
      let date = today.getDate();
      var day_string = year+"-"+month+"-"+date;
      array_field.push("date");
      array_name.push(day_string);

      var sql = 'insert into WriteData ('+array_field[0]+','+array_field[1]+','+array_field[2]+','+array_field[3]+','+array_field[4]+','+array_field[5]+') values (?, ?, ?, ?, ?, ?)';
       dbc.query(sql, [array_name[0],array_name[1],array_name[2],array_name[3],array_name[4],array_name[5]], function(err,result,field){
         if(err){
            console.log(err);
         }
      });
   });

   res.write("OK!");
   res.end();
});



app.post('/upload', upload.array('uploadFile'), function (req, res) {
    var filesLength = req.files.length;
    var uploadCnt = 0;
 
    if (filesLength <= 0) {
        res.status(500).end();
    } else {
        for (var i = 0; i < filesLength; i++) {
            imageUpload(req.files[i]);
            uploadCnt += 1;
 
            if (uploadCnt == filesLength) {
                res.status(200).end();
            }
        }
    }
});


function imageUpload(files) {
    fs.readFile(files.path, function (err, data) {
        var filePath = __dirname + '\\uploadFolder\\' + files.originalname;
        fs.writeFile(filePath, data, function (error) {
            if (error) {
                throw error;
            } else {
                fs.unlink(files.path, function (removeFileErr) {
                    if (removeFileErr) {
                        throw removeFileErr;
                    }
                });
            }
        });
    });
}