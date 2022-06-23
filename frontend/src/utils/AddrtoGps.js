import axios from "axios";

export const AddrtoGps = (address, callback) => {
    const gps = {
        lat: 0.0,
        lng: 0.0
    }

    const headers = {
        "mint-token": localStorage.getItem("mint-token")
    }

    axios.get("/api/out/a2g/" + address, { headers }).then(response => {
        const data = response.data;
        if (data.status === 200) {
            gps.lat = data.result.lat;
            gps.lng = data.result.lng;
            callback(gps);
        } else {
            callback(gps);
        }
    }).catch(error => {
        console.log(error.message);
        callback(gps);
    });
}