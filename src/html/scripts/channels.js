let channelsMenuButton = document.getElementById("channelsMenuButton");
channelsMenuButton.onclick = () => {
        let request = new XMLHttpRequest();
        request.open("GET", "http://localhost:8088/channels", true);
        request.onreadystatechange = function () {
            if (this.readyState === this.DONE && request.readyState === 4) {

                // let channels = JSON.parse(request.responseText).arrayList;

                alert(request.responseText);
            }
        }
        request.send();

}




