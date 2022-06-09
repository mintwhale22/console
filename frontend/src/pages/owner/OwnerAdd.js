import React, {useState} from 'react'
import {
    CButton,
    CCard,
    CCardBody,
    CCardTitle,
    CCol,
    CFormInput,
    CInputGroup,
    CInputGroupText,
    CRow
} from "@coreui/react";
import CIcon from "@coreui/icons-react";
import {cilSearch, cilUser, cilUserPlus} from "@coreui/icons";
import {useNavigate} from "react-router-dom";
import {isEmail} from "src/utils/Regexs";

const OwnerAdd = () => {
    const navigate = useNavigate();
    const isEmailCheck = false;
    const successEmail = "";

    const {email, setEmail} = useState("");

    const checkEmail = () => {
        if (!isEmailCheck) {
            if (!isEmail(email)) {
                window.alert("이메일 형식이 맞지 않습니다.\n다시 입력해주세요.");
            }
        }
    }

    const gotoBack = () => {
        navigate(-1);
    }

    return (
        <div>
            <CCard>
                <CCardBody>
                    <CCardTitle>
                        <CIcon icon={cilUser}/> 상점주 정보
                    </CCardTitle>
                    <CRow>
                        <CCol>
                            <CInputGroup>
                                <CInputGroupText id="basic-addon1">이메일</CInputGroupText>
                                <CFormInput placeholder="이메일을 입력해 주세요." aria-label="email"
                                            aria-describedby="basic-addon1"
                                            onChange={({target: {value}}) => setEmail(value)} value={email}/>
                                <CButton onClick={checkEmail}>중복확인</CButton>
                            </CInputGroup>
                        </CCol>
                    </CRow>
                </CCardBody>
            </CCard>
            <div className="text-center mt-5 d-md-block">
                <CButton color="dark" onClick={gotoBack}>이전으로</CButton>
                <CButton className="ms-3"><CIcon icon={cilUserPlus}/> 새로등록</CButton>
            </div>
        </div>
    )
}

export default OwnerAdd