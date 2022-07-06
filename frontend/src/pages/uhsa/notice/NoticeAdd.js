import React, {useEffect, useState} from 'react'
import {
    CButton,
    CCard,
    CCardBody,
    CCol,
    CFormInput, CFormTextarea,
    CInputGroup,
    CInputGroupText,
    CRow
} from "@coreui/react";
import CIcon from "@coreui/icons-react";
import {cilHouse, cilSearch, cilUser, cilUserPlus} from "@coreui/icons";
import {useNavigate, useParams} from "react-router-dom";
import DatePicker, {registerLocale, setDefaultLocale} from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import ko from 'date-fns/locale/ko';
import axios from "axios";
import queryString from "query-string";

const NoticeAdd = () => {
    let search = window.location.search ? window.location.search : window.location.hash;
    search = search.indexOf("?") > -1 ? search.split("?")[1] : "";
    const {pg, st} = queryString.parse(search);

    const {noticeSeq} = useParams();

    const navigate = useNavigate();
    const isEmailCheck = false;

    const [title, setTitle] = useState("");
    const [infos, setInfos] = useState("");

    const sendNotice = async () => {
        let error = false;
        let message = "";

        if (title === "" && !error) {
            error = true;
            message = "제목이 없습니다.";
        }
        if (infos === "" && !error) {
            error = true;
            message = "상세내용이 없습니다.";
        }

        if (!error) {

            if (noticeSeq) {
                try {
                    const response2 = await axios.post("/api/notice/edit", {
                        seq: noticeSeq,
                        title: title,
                        contents: infos,
                        status: 1
                    }, {
                        headers: {
                            "Content-Type": "application/json",
                            "mint-token": localStorage.getItem("mint-token")
                        }
                    });

                    const data2 = response2.data;
                    console.log(data2);

                    if (data2.status === 200) {
                        alert("사용자가 수정되었습니다.");
                    } else {
                        error = true;
                        message = "사용자 수정중 에러가 발생하였습니다.";
                    }
                } catch (error) {
                    console.log(error.message);
                    error = true;
                    message = "사용자 수정중 에러가 발생하였습니다.";
                }
            } else {
                try {
                    const response2 = await axios.post("/api/notice/add", {
                        title: title,
                        contents: infos,
                        status: 1
                    }, {
                        headers: {
                            "Content-Type": "application/json",
                            "mint-token": localStorage.getItem("mint-token")
                        }
                    });

                    const data2 = response2.data;
                    console.log(data2);

                    if (data2.status === 200) {
                        alert("사용자가 등록되었습니다.\n목록으로 이동합니다.");
                        navigate("/uhsa/notice?pg=" + pg + "&st=" + encodeURIComponent(st));
                    } else {
                        error = true;
                        message = "사용자 등록중 에러가 발생하였습니다.";
                    }
                } catch (error) {
                    console.log(error.message);
                    error = true;
                    message = "사용자 등록중 에러가 발생하였습니다.";
                }
            }
        }

        if (error) {
            alert(message);
        }
    }

    const gotoBack = () => {
        navigate("/uhsa/notice?pg=" + pg + "&st=" + encodeURIComponent(st));
    }

    registerLocale('ko', ko);

    const loadData = async () => {
        let error = false;
        let message = "";
        try {
            const response = await axios.post("/api/notice/info", {
                seq: noticeSeq
            }, {
                headers: {
                    "Content-Type": "application/json",
                    "mint-token": localStorage.getItem("mint-token")
                }
            });

            const data = response.data;

            if (data.status === 200) {
                setTitle(data.result.title);
                setInfos(data.result.contents.toString());
            } else {
                error = true;
                message = "공지사항을 읽어오는 동안 오류가 발생하였습니다.";
            }

        } catch (e) {
            console.log(e.message);
            error = true;
            message = "공지사항을 읽어오는 동안 오류가 발생하였습니다.";
        }

        if (error) {
            alert(message);
        }
    }

    useEffect(() => {
        console.log(noticeSeq);
        if (noticeSeq) {
            loadData();
        }
    }, []);

    return (
        <div>
            <CCard>
                <CCardBody>
                    <CRow>
                        <CInputGroup>
                            <CInputGroupText id="basic-addon1" className="col-2">제목</CInputGroupText>
                            <CFormInput placeholder="제목을 입력해 주세요."
                                        onChange={({target: {value}}) => setTitle(value)} value={title}/>
                        </CInputGroup>
                    </CRow>
                    <CRow className="mt-3">
                        <CInputGroup>
                            <CInputGroupText id="basic-addon2" className="col-2">내용</CInputGroupText>
                            <CFormTextarea rows="5"
                                           onChange={({target: {value}}) => setInfos(value)}>{infos}</CFormTextarea>
                        </CInputGroup>
                    </CRow>
                </CCardBody>
            </CCard>
            <div className="text-center p-5 d-md-block">
                <CButton color="dark" onClick={gotoBack}>목록으로</CButton>
                <CButton className="ms-3" onClick={sendNotice}><CIcon
                    icon={cilUserPlus}/> {noticeSeq ? "공지사항 수정하기" : "공지사항 새로등록"}</CButton>
            </div>
        </div>
    )
}

export default NoticeAdd