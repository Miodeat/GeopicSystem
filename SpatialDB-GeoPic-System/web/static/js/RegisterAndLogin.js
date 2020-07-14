/**
 * 注册登录
 *
 */
RegisterAnfLogin = function (options) {

}

/**
 * 注册
 */
RegisterAnfLogin.prototype.register = function () {
    let me = this;
    let username = $(".usernameInput").val();
    let password = $(".passwordInput").val();
    let passwordSure = $(".passwordInputSure").val();
    console.log(username);
    console.log(password);
    me.submitRegisterDataToLoginServlet(username,password);
};
/**
 * 获取注册信息并进行提交
 * @param username 用户名
 * @param password 用户设置的密码
 */
RegisterAnfLogin.prototype.submitRegisterDataToLoginServlet = function(username,password){
    $.ajax({
        type:"POST",
        // dataType:"json",
        url:"/SpatialDB-GeoPic-System/UserRegisterServlet",
        data:{
            "username":username,
            "password":password
        },
        success:function (res) {
            var json =  typeof res =='string'?JSON.parse(res):res;
            var result = json.message;
            if(result=="success"){
                window.location.href="static/html/login.html";
            }else if(result=="failure"){
                alert("注册失败")
            }else if(result=="failure,用户名已被占用"){
                alert("注册失败，用户名已被占用")
            }
        }
    });
};

/**
 * 注册成功后显示登录模态框
 */
RegisterAnfLogin.prototype.loginDirect = function () {
    $(".login").show();
};

/**
 * 登录时用户账号密码检测
 */
RegisterAnfLogin.prototype.loginCheck = function () {
    let me = this;
    console.log("hello")
    let username = $(".username").val();
    let password = $(".password").val();
    console.log(username);
    console.log(password);
    $.ajax({
        type: "POST",
        // dataType:"json",
        url: "/SpatialDB-GeoPic-System/UserLoginServlet",
        data: {
            "username": username,
            "password": password
        },
        success: function (res) {
            let json =  typeof res =='string'?JSON.parse(res):res;
            let result = json.message;
            if (result=="success"){
                var userDbname = json.userDbName;
                window.location.href="../html/index.html?userDbName="+userDbname;
            }else {
                alert("用户名或者密码不正确")
            }
        },
        error:function (err) {
            console.log(err)
        }
    });
};