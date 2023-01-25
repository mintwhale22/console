import React, {useEffect, useState} from 'react'
import {
    CButton,
    CCard,
    CCardBody,
    CCol, CFormCheck,
    CFormInput, CFormSelect, CFormTextarea,
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

    const [title, setTitle] = useState("");
    const [infos, setInfos] = useState("");
    const [status, setStatus] = useState(1);
    const [owner, setOwner] = useState(1);
    const [user, setUser] = useState(1);

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
                        status: status,
                        owner: owner,
                        user: user,
                    }, {
                        headers: {
                            "Content-Type": "application/json",
                            "mint-token": localStorage.getItem("mint-token")
                        }
                    });

                    const data2 = response2.data;

                    if (data2.status === 200) {
                        alert("공지사항이 수정되었습니다.");
                    } else {
                        error = true;
                        message = "공지사항 수정중 에러가 발생하였습니다.";
                    }
                } catch (error) {
                    console.log(error.message);
                    error = true;
                    message = "공지사항 수정중 에러가 발생하였습니다.";
                }
            } else {
                try {
                    const response2 = await axios.post("/api/notice/add", {
                        title: title,
                        contents: infos,
                        owner: owner,
                        user: user,
                        status: status,
                    }, {
                        headers: {
                            "Content-Type": "application/json",
                            "mint-token": localStorage.getItem("mint-token")
                        }
                    });

                    const data2 = response2.data;

                    if (data2.status === 200) {
                        alert("공지사항이 등록되었습니다.\n목록으로 이동합니다.");
                        navigate("/uhsa/notice?pg=" + pg + "&st=" + encodeURIComponent(st));
                    } else {
                        error = true;
                        message = "공지사항 등록중 에러가 발생하였습니다.";
                    }
                } catch (error) {
                    console.log(error.message);
                    error = true;
                    message = "공지사항 등록중 에러가 발생하였습니다.";
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
                setStatus(data.result.status);
                setTitle(data.result.title);
                setInfos(data.result.contents);
                setOwner(data.result.owner);
                setUser(data.result.user);
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
                            <CFormTextarea rows="15"
                                           onChange={({target: {value}}) => setInfos(value)} value={infos} />
                        </CInputGroup>
                    </CRow>
                    <CRow className="mt-3">
                        <CInputGroup>
                            <CInputGroupText id="basic-addon2" className="col-2">상태</CInputGroupText>
                            <div className="form-control">
                                <CFormSelect aria-label="status" onChange={({target: {value}}) => setStatus(value)}
                                             className="border-0 p-0">
                                    <option value="1" selected={status === 1}>사용</option>
                                    <option value="9" selected={status === 9}>사용안함</option>
                                </CFormSelect>
                            </div>
                        </CInputGroup>
                    </CRow>
                    <CRow className="mt-3">
                        <CInputGroup>
                            <CInputGroupText id="basic-addon1" className="col-2">상점주 보이기</CInputGroupText>
                            <div className="form-control col-4">
                                <CFormCheck checked={owner === 1} type="checkbox" value="1" onChange={() => {
                                    setOwner(owner === 1 ? 9 : 1);
                                }}/>
                            </div>
                            <CInputGroupText id="basic-addon1" className="col-2">사용자 보이기</CInputGroupText>
                            <div className="form-control col-4">
                                <CFormCheck checked={user === 1} type="checkbox" value="1" onChange={() => {
                                    setUser(user === 1 ? 9 : 1);
                                }}/>
                            </div>
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
