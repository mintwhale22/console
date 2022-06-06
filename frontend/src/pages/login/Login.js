import React, {useState} from 'react'
import {
    CButton,
    CCard,
    CCardBody,
    CCardGroup,
    CCol,
    CContainer,
    CForm,
    CFormInput,
    CInputGroup,
    CInputGroupText,
    CRow,
} from '@coreui/react'
import CIcon from '@coreui/icons-react'
import {cilEnvelopeOpen, cilLockLocked, cilUser} from '@coreui/icons'
import {isEmail, isPassword} from 'src/utils/Regexs'
import axios from "axios";
import {useNavigate} from "react-router-dom";

const Login = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [buttontitle, setButtontitle] = useState("관리자 로그인");

    let isProcessing = false;
    const navigate = useNavigate();

    const handleClick = () => {
        if (isProcessing) {
            alert("처리중입니다.");
        } else {
            let rtnvalue = {
                error: false,
                message: ""
            };

            console.log("data : ", email, " / ", password);

            if (email === "" && !rtnvalue.error) {
                rtnvalue.error = true;
                rtnvalue.message = "이메일 값이 비었습니다. 이메일을 입력해주세요.";
            }

            if (!isEmail(email) && !rtnvalue.error) {
                rtnvalue.error = true;
                rtnvalue.message = "이메일 형식이 아닙니다. 다시 입력해 주세요.";
            }

            if (password === "" && !rtnvalue.error) {
                rtnvalue.error = true;
                rtnvalue.message = "비밀번호 값이 비었습니다. 비밀번호를 입력해주세요.";
            }

            if (!isPassword(password) && !rtnvalue.error) {
                rtnvalue.error = true;
                rtnvalue.message = "비밀번호 형식이 아닙니다.\n비밀번호는 8~20글자로 숫자와 특수문자[~`!@#$%\^&*()-+=]가 1글자 이상 포함되어야 합니다.";
            }

            if (rtnvalue.error) {
                alert(rtnvalue.message);
            } else {
                isProcessing = true;
                setButtontitle("로그인 처리중...");
                const headers = {
                    'Context-Type': "application/json"
                }
                axios.post('/api/login', {email: email, pass: password}, { headers }).then(response => {
                    const data = response.data;
                    if (data.status === 200) {
                        localStorage.setItem("mint-token", data.result.toString());
                        // navigate("/");
                        window.location.reload();
                    } else {
                        alert(data.message);
                    }

                    isProcessing = false;
                    setButtontitle("관리자 로그인");
                }).catch(error => {
                    console.log(error);
                    isProcessing = false;
                    setButtontitle("관리자 로그인");
                })
            }
        }
    }

    return (
        <div className="bg-light min-vh-100 d-flex flex-row align-items-center">
            <CContainer>
                <CRow className="justify-content-center">
                    <CCol md={6}>
                        <CCardGroup>
                            <CCard className="p-4">
                                <CCardBody>
                                    <CForm>
                                        <h1>로그인</h1>
                                        <p className="text-medium-emphasis">관리자 정보를 입력해 주세요.</p>
                                        <CInputGroup className="mb-3">
                                            <CInputGroupText>
                                                <CIcon icon={cilEnvelopeOpen}/>
                                            </CInputGroupText>
                                            <CFormInput placeholder="이메일 입력" autoComplete="username" value={email}
                                                        onChange={({target: {value}}) => setEmail(value)}/>
                                        </CInputGroup>
                                        <CInputGroup className="mb-4">
                                            <CInputGroupText>
                                                <CIcon icon={cilLockLocked}/>
                                            </CInputGroupText>
                                            <CFormInput
                                                type="password"
                                                placeholder="비밀번호 입력"
                                                autoComplete="current-password" value={password}
                                                onChange={({target: {value}}) => setPassword(value)}
                                            />
                                        </CInputGroup>
                                        <CRow>
                                            <CCol xs={6}>
                                                <CButton color="primary" className="px-6" onClick={handleClick}>
                                                    {buttontitle}
                                                </CButton>
                                            </CCol>
                                        </CRow>
                                    </CForm>
                                </CCardBody>
                            </CCard>
                        </CCardGroup>
                    </CCol>
                </CRow>
            </CContainer>
        </div>
    )
}

export default Login
