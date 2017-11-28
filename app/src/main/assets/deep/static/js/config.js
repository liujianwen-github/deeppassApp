/**
 * 这个是为了方便看的，实际使用的是src目录下的config,如有需要可以把config依赖替换为当前文件（需要更新）
 */
import axios from 'axios'
export default{
  // HOST: 'http://192.168.1.239:8080/',
  HOST: 'http://demo.deepdot.cn/',
  // HOST: 'localshot:8080/',
  // userkey: '5f84bb25_4ea8_42c2_a6bf_744b0bb574a9',
  userkey: '391cb26c_45f3_4817_86f8_644e293cce60',
  deviceId: 'aaa-a01-001',
  minImageCount: 3,
  maxImageCount: 5,
  pageShow: 5,
  dayBefore: {
    // 筛选规则，不能选择今天之后的时间
    disabledDate(date) {
          return date > Date.now()
        }
  },
  cropImg: {
    img: '',
    info: true,
    size: 1,
    outputType: 'jpeg',
    canScale: false,
    autoCrop: true,
    // 只有自动截图开启 宽度高度才生效
    autoCropWidth: 300,
    autoCropHeight: 300,
    // 开启宽度和高度比例
    fixed: true,
    fixedNumber: [1,1]
  },
  addDate: function (date) {
    if (Number(date) <= 10) {
      return '0' + date
    } else {
      return date
    }
  },
  dayBefor: function (date) {
    let daybefore = new Date(date)
    return daybefore
  },
  // yyyy-mm-dd格式转换为毫秒
  changeDateType: function (msg) {
    let date = new Date(msg).getTime()
    return date
  },
  /**
   * @Author    liujianwen
   * @DateTime  2017-08-10
   * @copyright [axios配置文件]
   */
  axiosCon: function () {
    axios.defaults.baseURL = this.HOST
    axios.defaults.timeout = 5000
    axios.defaults.responseType = 'json'
    axios.defaults.xsrfCookieName = '111'
    axios.defaults.xsrfHeaderName = 'demo'
    axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded'
  },
  /**
   * @Author    liujianwen
   * @DateTime  2017-08-10
   * @copyright [深拷贝]
   * @param     {[object]}      source [被拷贝对象]
   * @return    {[object]}             [拷贝对象]
   */
  deepCopy: function (source) {
    let result = {}
    for (var key in source) {
      result[key] = typeof source[key] === 'object' ? this.deepCopy(source[key]) : source[key]
    }
    return result
  },
  /**
   * @Author    liujianwen
   * @DateTime  2017-08-10
   * @copyright [读取文件，返回base64编码]
   * @param     {[object]}      source   [文件资源]
   * @param     {Function}    callback [获取编码后的回调函数]
   * @return    {[void]}               
   */
  readFile: function (source,callback){
    const _this = this
    const reader = new FileReader()
    console.log(source)
    reader.readAsDataURL(source)
    reader.onload = function (e){
      console.log(this)
      console.log(e)
      _this.video2img(e.target.result,callback)
    }
  },
  /**
   * @Author    liujianwen
   * @DateTime  2017-08-10
   * @param     {[String]}      source [视频base64编码]
   * @return    {[Array]}             [图片数组]
   */
  video2img: function (file,callback){
    // console.log(file)
    //创建视频元素
    let video = document.createElement('video')
    // video.style.maxWidth ='120px'
    // video.style.maxHeight ='120px'
    video.src = file
    video.autoplay = true
    console.log(video.videoHeight)

    // 创建canvas元素
    video.onloadedmetadata = function (){
      console.log(Math.round(video.duration))
      console.log(video.style)
      console.log(video.height)
      //视频时长
      var videoTime = video.duration
      // 待返回的图片数组
      var imgs = []
      var intervla =  setInterval(function(){
        // 新建canvas开始截图，1000毫秒一次
        let cvs = document.createElement('canvas')
        cvs.width = 120
        cvs.height= 120
        cvs.getContext('2d').drawImage(video,0,0,100,120)
        imgs.push(cvs.toDataURL().split(',')[1])
      },1000)
      setTimeout(function(){
        console.log(imgs)
        clearInterval(intervla)
        // 截图结束，回调
        callback(imgs)
      },Math.round(videoTime)*1000)
    }
  }
}
