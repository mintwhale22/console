import React, {useState} from 'react'
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";

const Users = () => {

    // photos, setPhotos 비구조화 할당
    let [photos, setPhotos] = useState([]);

    // 통신 메서드
    function searchApi() {
        setPhotos([{id: 11, title: "제목1"}, {id: 12, title: "제목22"}]);
    }

    // 조회 데이터 존재할 경우
    if (photos.length > 0) {
        return (
            photos.map(photo => (
                <div key={photo.id}>
                    <p>title : {photo.title}</p>
                </div>
            ))
        );
    } else { // 조회 데이터 존재하지 않을 경우
        return (
            <div>
                <button onClick={searchApi}> 불러오기</button>
            </div>
        )
    }
}

export default Users