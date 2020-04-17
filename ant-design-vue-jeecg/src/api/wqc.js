export function downFile(url,parameter){
  return axios({
    url: url,
    params: parameter,
    method:'get' ,
    responseType: 'blob'
  })
}

//打印
export function debug(info) {
  console.log('*************************');
  console.log('*************************');
  console.log(info);
  console.log('*************************');
  console.log('*************************');
}

//文件名
export function getFileName(path) {
  if(path.lastIndexOf("\\") >= 0) {
    let reg = new RegExp("\\\\","g");
    path = path.replace(reg, "/");
  }

  return path.substring(path.lastIndexOf("/") + 1);
}

//今天多少号
export function getDate(dateTime) {
  return new Date(Date.parse(dateTime)).getDate();
}

//今天周几
export function getDay(dateTime) {
  let day = new Date(Date.parse(dateTime)).getDay();
  let weeks = ["周日", "周一", "周二", "周三", "周四", "周五", "周六"];

  return weeks[day];
}

//转换日期为yyyy-mm-dd
export function dateFormat(dateTime) {
  let time = new Date(Date.parse(dateTime));
  let year = time.getFullYear();
  let month = time.getMonth() + 1;
  month = month < 10 ? '0' + month : month;
  let date = time.getDate();
  date = date < 10 ? '0' + date : date;

  return year + '-' + month + '-' + date;
}