import React, {useState} from 'react'
import {
    CButton,
    CCard,
    CCardBody, CCardText,
    CCardTitle,
    CCol, CFormCheck,
    CFormInput, CFormSelect, CFormText,
    CInputGroup,
    CInputGroupText,
    CRow
} from "@coreui/react";
import CIcon from "@coreui/icons-react";
import {cilHouse, cilSearch, cilUser, cilUserPlus} from "@coreui/icons";
import {useNavigate} from "react-router-dom";
import {isEmail} from "src/utils/Regexs";
import DatePicker, {registerLocale, setDefaultLocale} from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import ko from 'date-fns/locale/ko';
import {addYears} from "date-fns";
import OwnerStore, {getData} from "./OwnerStore";

const OwnerAdd = () => {
    const navigate = useNavigate();
    const isEmailCheck = false;

    const [email, setEmail] = useState("");
    const [name, setName] = useState("");
    const [nik, setNik] = useState("");
    const [password, setPassword] = useState("");
    const [password2, setPassword2] = useState("");
    const [birth, setBirth] = useState(null);
    const [sex, setSex] = useState(3);
    const [job, setJob] = useState(0);
    const [level, setLevel] = useState(1);
    const [status, setStatus] = useState(1);

    let [stores, setStores] = useState([]);
    const [printlist, setPrintList] = useState(OwnerStore({data : stores}));

    const checkEmail = () => {
        if (!isEmailCheck) {
            if (!isEmail(email)) {
                window.alert("이메일 형식이 맞지 않습니다.\n다시 입력해주세요.");
            }
        }
    }

    const addStore = () => {
        let array = getData();
        array.push({seq: array.length + 1, sname: "", sinfo: "", status: 1, files: []});
        setStores(array);
        setPrintList(OwnerStore({data : array}));
        console.log(stores);
    }

    const gotoBack = () => {
        navigate(-1);
    }

    registerLocale('ko', ko);

    return (
        <div>
            <CCard>
                <CCardBody>
                    <CCardTitle className="fw-bold">
                        <CIcon icon={cilUser}/> 상점주 정보
                    </CCardTitle>
                    <CRow>
                        <CCol>
                            <CInputGroup>
                                <CInputGroupText id="basic-addon1" className="col-4">이메일</CInputGroupText>
                                <CFormInput placeholder="이메일을 입력해 주세요." aria-label="email"
                                            aria-describedby="basic-addon1"
                                            onChange={({target: {value}}) => setEmail(value)} value={email}
                                            onBlur={checkEmail}/>
                            </CInputGroup>
                        </CCol>
                        <CCol>
                            <CInputGroup>
                                <CInputGroupText id="basic-addon6" className="col-4">생일</CInputGroupText>
                                <div className="form-control">
                                    <DatePicker selected={birth} onChange={(date: Date) => setBirth(date)}
                                                minDate={addYears(new Date(), -80)}
                                                maxDate={addYears(new Date(), -18)}
                                                dateFormat="yyyy-MM-dd"
                                                isClearable={true}
                                                className="border-0" locale="ko"/>
                                </div>
                            </CInputGroup>
                        </CCol>
                    </CRow>
                    <CRow className="mt-3">
                        <CCol>
                            <CInputGroup>
                                <CInputGroupText id="basic-addon2" className="col-4">성명</CInputGroupText>
                                <CFormInput placeholder="성명을 입력해 주세요." aria-label="name"
                                            aria-describedby="basic-addon2"
                                            onChange={({target: {value}}) => setName(value)} value={name}/>
                            </CInputGroup>
                        </CCol>
                        <CCol>
                            <CInputGroup>
                                <CInputGroupText id="basic-addon3" className="col-4">별칭</CInputGroupText>
                                <CFormInput placeholder="별칭을 입력해 주세요." aria-label="nik"
                                            aria-describedby="basic-addon3"
                                            onChange={({target: {value}}) => setNik(value)} value={nik}/>
                            </CInputGroup>
                        </CCol>
                    </CRow>
                    <CRow className="mt-3">
                        <CCol>
                            <CInputGroup>
                                <CInputGroupText id="basic-addon4" className="col-4">비밀번호</CInputGroupText>
                                <CFormInput placeholder="비밀번호를 입력해 주세요." aria-label="name"
                                            aria-describedby="basic-addon4"
                                            type="password"
                                            onChange={({target: {value}}) => setPassword(value)} value={password}/>
                            </CInputGroup>
                        </CCol>
                        <CCol>
                            <CInputGroup>
                                <CInputGroupText id="basic-addon5" className="col-4">비밀번호 확인</CInputGroupText>
                                <CFormInput placeholder="비밀번호 확인을 입력해 주세요." aria-label="nik"
                                            aria-describedby="basic-addon5"
                                            type="password"
                                            onChange={({target: {value}}) => setPassword2(value)} value={password2}/>
                            </CInputGroup>
                        </CCol>
                    </CRow>
                    <CRow className="mt-3">
                        <CCol>
                            <CInputGroup>
                                <CInputGroupText id="basic-addon7" className="col-4">성별</CInputGroupText>
                                <div className="form-control">
                                    <CFormCheck inline type="radio" name="sex" id="sex1" label="남자" value="1"
                                                onClick={({target: {value}}) => setSex(value)}
                                                defaultChecked={sex === 1}/>
                                    <CFormCheck inline type="radio" name="sex" id="sex2" label="여자" value="2"
                                                onClick={({target: {value}}) => setSex(value)}
                                                defaultChecked={sex === 2}/>
                                    <CFormCheck inline type="radio" name="sex" id="sex3" label="무관" value="3"
                                                onClick={({target: {value}}) => setSex(value)}
                                                defaultChecked={sex === 3}/>
                                </div>
                            </CInputGroup>
                        </CCol>
                        <CCol>
                            <CInputGroup>
                                <CInputGroupText id="basic-addon8" className="col-4">직업</CInputGroupText>
                                <div className="form-control">
                                    <CFormSelect aria-label="job" onChange={({target: {value}}) => setJob(value)}
                                                 className="border-0 p-0">
                                        <option value="">없음</option>
                                        <option value="1" selected={job === 1}>직업1</option>
                                        <option value="2" selected={job === 2}>직업2</option>
                                        <option value="3" selected={job === 3}>직업3</option>
                                        <option value="4" selected={job === 4}>직업4</option>
                                        <option value="5" selected={job === 5}>직업5</option>
                                    </CFormSelect>
                                </div>
                            </CInputGroup>
                        </CCol>
                    </CRow>
                    <CRow className="mt-3">
                        <CCol>
                            <CInputGroup>
                                <CInputGroupText id="basic-addon7" className="col-4">레벨</CInputGroupText>
                                <div className="form-control">
                                    <CFormSelect aria-label="level" onChange={({target: {value}}) => setLevel(value)}
                                                 className="border-0 p-0">
                                        <option value="1" selected={level === 1}>레벨1</option>
                                        <option value="2" selected={level === 2}>레벨2</option>
                                        <option value="3" selected={level === 3}>레벨3</option>
                                        <option value="4" selected={level === 4}>레벨4</option>
                                        <option value="5" selected={level === 5}>레벨5</option>
                                        <option value="6" selected={level === 6}>레벨6</option>
                                        <option value="7" selected={level === 7}>레벨7</option>
                                        <option value="8" selected={level === 8}>레벨8</option>
                                        <option value="9" selected={level === 9}>레벨9</option>
                                    </CFormSelect>
                                </div>
                            </CInputGroup>
                        </CCol>
                        <CCol>
                            <CInputGroup>
                                <CInputGroupText id="basic-addon8" className="col-4">상태</CInputGroupText>
                                <div className="form-control">
                                    <CFormSelect aria-label="status" onChange={({target: {value}}) => setStatus(value)}
                                                 className="border-0 p-0">
                                        <option value="1" selected={status === 1}>정상</option>
                                        <option value="5" selected={status === 5}>차단</option>
                                        <option value="9" selected={status === 9}>탈퇴</option>
                                    </CFormSelect>
                                </div>
                            </CInputGroup>
                        </CCol>
                    </CRow>
                </CCardBody>
            </CCard>
            <CCard className="mt-5">
                <CCardBody>
                    <CCardTitle className="fw-bold">
                        <CRow>
                            <CCol>
                                <CIcon icon={cilHouse}/> 상점 정보
                            </CCol>
                            <CCol className="text-end">
                                <CButton className="ms-3" onClick={addStore}><CIcon icon={cilHouse}/> 상점추가</CButton>
                            </CCol>
                        </CRow>
                    </CCardTitle>
                    <CRow>
                        {printlist}
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