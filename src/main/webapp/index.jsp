<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="style.css">
    <title>Document</title>
</head>
<body>
<div class="box">
    <form class="login" action="login" method="POST" id="loginForm">
        <div class="title">
            <h1 class="title">LOGIN</h1>
            <P>Insert your information here</P>
        </div>
        <div class="inps">
            <div class="in">
                <img src="imgs/user.svg" alt="">
                <input type="text" name="username" tabindex="1">
            </div>
            <div class="in">
                <img src="imgs/password.svg" alt="">
                <input type="password" name="password" tabindex="2">
                <img src="imgs/eye.svg" alt="" class="eye">
            </div>
            <div class="but" tabindex="3" id="sub" onclick="document.getElementById('loginForm').submit()">
                <h1>Log In</h1>
                <img src="imgs/login.svg" alt="">
            </div>
        </div>
        <div class="extra">
        </div>
    </form>

    <div class="pic">
        <img src="imgs/sec.png" alt="">
    </div>
</div>
<script>
    document.getElementById("sub").addEventListener("keydown", function(event) {
        if (event.key === "Enter") {
            document.getElementById("sub").click();
        }
    });
</script>
</body>
</html>