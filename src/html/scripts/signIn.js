let signInMenuButton = document.getElementById("signInMenuButton");

signInMenuButton.onclick = () => {
    document.getElementById("signIn").style.display = "block";
    document.getElementById("signUp").style.display = "none";

}



let login = document.getElementById("loginStart");

login.onclick = () => {
    let nameBox = document.getElementById("usernameSignIn");
    let passwordBox = document.getElementById("passwordSignIn");

    let request = new XMLHttpRequest();
    request.open("GET", "http://localhost:8000/auth", true);

    let data = {
        username: nameBox.value,
        password: passwordBox.value,
    };




    let message = {
        command: SIGN_IN,
        data: data,
    };



    request.send(JSON.stringify(data));

    request.onreadystatechange = function () {
        if (this.readyState === this.DONE && request.readyState === 4) {
            alert(request.responseText);
        }
    }

};