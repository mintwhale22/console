import React, {useEffect, useState} from 'react'
import {
    CButton,
    CCard,
    CCardBody,
    CCardTitle,
    CCol, CFormCheck,
    CFormInput,
    CFormTextarea,
    CInputGroup,
    CInputGroupText,
    CRow
} from "@coreui/react";
import CIcon from "@coreui/icons-react";
import {cilSettings, cilUserPlus} from "@coreui/icons";
import axios from "axios";

const Appset = () => {

    const [android, setAndroid] = useState({ver: "", code: 0, message: "", download: "", update: 0});
    const [ios, setIos] = useState({ver: "", code: 0, message: "", download: "", update: 0});

    const setValue = (type, field) => e => {
        //console.log(type, field, e.target.value);

        let setval = type === 1 ? android : ios;

        switch (field) {
            case "ver" :
                setval.ver = e.target.value;
                break;
            case "code" :
                setval.code = e.target.value;
                break;
            case "message" :
                setval.message = e.target.value;
                break;
            case "download" :
                setval.download = e.target.value;
                break;
            case "update" :
                setval.update = e.target.value === 'on' ? 1 : 0;
                break;
        }
        //console.log(setval);

        if (type === 1) {
            setAndroid((prevState) => ({
                ...prevState, setval
            }));
        } else {
            setIos((prevState) => ({
                ...prevState, setval
            }));
        }
    }

    const snedAppset = async (apptype) => {
        //console.log("snedAppset :: " + apptype);
        let error = false;
        let message = "";

        try {
            const response = await axios.post("/api/appset/edit", (apptype === 1 ? android : ios), {
                headers: {
                    "Content-Type": "application/json",
                    "mint-token": localStorage.getItem("mint-token")
                }
            });

            const data = response.data;

            if (data.status === 200) {
                message = (apptype === 1 ? "Android" : "IOS") + " 앱설정이 저장되었습니다.";
            } else {
                message = (apptype === 1 ? "Android" : "IOS") + " 앱설정을 저장하는 동안 오류가 발생하였습니다.";
            }

        } catch (e) {
            //console.log(e.message);
            error = true;
            message = (apptype === 1 ? "Android" : "IOS") + " 앱설정을 저장하는 동안 오류가 발생하였습니다.";
        }

        alert(message);
    }

    const loadApp = async () => {
        //console.log("loadApp");
        let error = false;
        let message = "";
        
        try {
            const response1 = await axios.post("/api/appset/list", {
                "type": 1
            }, {
                headers: {
                    "Content-Type": "application/json",
                    "mint-token": localStorage.getItem("mint-token")
                }
            });

            const data1 = response1.data;
            //console.log(data1);

            if (data1.status === 200) {
                const result = data1.result;
                setAndroid(result);
            } else {
                error = true;
                message = "앱설정을 읽어오는 동안 오류가 발생하였습니다.";
            }

            const response2 = await axios.post("/api/appset/list", {
                    "type": 2
            }, {
                headers: {
                    "Content-Type": "application/json",
                    "mint-token": localStorage.getItem("mint-token")
                }
            });

            const data2 = response2.data;
            //console.log(data2);

            if (data2.status === 200) {
                const result = data2.result;
                setIos(result);
            } else {
                error = true;
                message = "앱설정을 읽어오는 동안 오류가 발생하였습니다.";
            }
        } catch (e) {
            //console.log(e.message);
            error = true;
            message = "앱설정을 읽어오는 동안 오류가 발생하였습니다.";
        }

        if (error) {
            alert(message);
        }
    }



    useEffect(() => {
        loadApp();
    }, []);

    return (
        <>
            <CCard>
                <CCardBody>
                    <CCardTitle className="fw-bold">
                        <CIcon icon={cilSettings}/> Android APP 셋팅
                    </CCardTitle>
                    <CRow>
                        <CCol>
                            <CInputGroup>
                                <CInputGroupText className="col-4">버전</CInputGroupText>
                                <CFormInput placeholder="버전을 입력해주세요."
                                            onChange={setValue(1, "ver")}
                                            value={android.ver}/>
                            </CInputGroup>
                        </CCol>
                        <CCol>
                            <CInputGroup>
                                <CInputGroupText className="col-4">버전코드</CInputGroupText>
                                <CFormInput placeholder="버전코드를 입력해주세요."
                                            onChange={setValue(1, "code")}
                                            value={android.code}/>
                            </CInputGroup>
                        </CCol>
                    </CRow>
                    <CRow className="mt-2">
                        <CCol>
                            <CInputGroup>
                                <CInputGroupText className="col-2">다운로드 URL</CInputGroupText>
                                <CFormInput placeholder="다운로드 URL을 입력해주세요."
                                            onChange={setValue(1, "download")}
                                            value={android.download}/>
                                <CInputGroupText className="col-2 align-middle">
                                    <CFormCheck
                                        onChange={setValue(1, "update")}
                                        checked={android.update}/>&nbsp;&nbsp;강제 업데이트
                                </CInputGroupText>
                            </CInputGroup>
                        </CCol>
                    </CRow>
                    <CRow className="mt-2">
                        <CCol>
                            <CInputGroup>
                                <CInputGroupText className="col-2">긴급메세지</CInputGroupText>
                                <CFormTextarea rows={3} onChange={setValue(1, "message")}
                                               value={android.message}/>
                            </CInputGroup>
                        </CCol>
                    </CRow>
                    <CRow className="mt-2">
                        <div className="text-center p-3 d-md-block">
                            <CButton className="ms-3" onClick={() => {
                                snedAppset(1);
                            }}>Android 설정저장</CButton>
                        </div>
                    </CRow>
                </CCardBody>
            </CCard>
            <CCard className="mt-4">
                <CCardBody>
                    <CCardTitle className="fw-bold">
                        <CIcon icon={cilSettings}/> IOS APP 셋팅
                    </CCardTitle>
                    <CRow>
                        <CCol>
                            <CInputGroup>
                                <CInputGroupText className="col-4">버전</CInputGroupText>
                                <CFormInput placeholder="버전을 입력해주세요."
                                            onChange={setValue(2, "ver")}
                                            value={ios.ver}/>
                            </CInputGroup>
                        </CCol>
                        <CCol>
                            <CInputGroup>
                                <CInputGroupText className="col-4">버전코드</CInputGroupText>
                                <CFormInput placeholder="버전코드를 입력해주세요."
                                            onChange={setValue(2, "code")}
                                            value={ios.code}/>
                            </CInputGroup>
                        </CCol>
                    </CRow>
                    <CRow className="mt-2">
                        <CCol>
                            <CInputGroup>
                                <CInputGroupText className="col-2">다운로드 URL</CInputGroupText>
                                <CFormInput placeholder="다운로드 URL을 입력해주세요."
                                            onChange={setValue(2, "download")}
                                            value={ios.download}/>
                                <CInputGroupText className="col-2 align-middle">
                                    <CFormCheck
                                        onChange={setValue(2, "update")}
                                        checked={ios.update}/>&nbsp;&nbsp;강제 업데이트
                                </CInputGroupText>
                            </CInputGroup>
                        </CCol>
                    </CRow>
                    <CRow className="mt-2">
                        <CCol>
                            <CInputGroup>
                                <CInputGroupText className="col-2">긴급메세지</CInputGroupText>
                                <CFormTextarea rows={3} onChange={setValue(2, "message")}
                                               value={(ios.message ? ios.message : "")}/>
                            </CInputGroup>
                        </CCol>
                    </CRow>
                    <CRow className="mt-2">
                        <div className="text-center p-3 d-md-block">
                            <CButton className="ms-3" onClick={() => {
                                snedAppset(2);
                            }}>IOS 설정저장</CButton>
                        </div>
                    </CRow>
                </CCardBody>
            </CCard>
        </>
    );
}

export default Appset