import React, {useEffect, useState} from 'react'
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
import {useNavigate, useParams} from "react-router-dom";
import {isEmail} from "src/utils/Regexs";
import DatePicker, {registerLocale, setDefaultLocale} from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import ko from 'date-fns/locale/ko';
import {addYears} from "date-fns";
import OwnerStore from "./OwnerStore";
import axios from "axios";
import queryString from "query-string";

const OwnerAdd = () => {
    let search = window.location.search ? window.location.search : window.location.hash;
    search = search.indexOf("?") > -1 ? search.split("?")[1] : "";
    const {pg, st} = queryString.parse(search);

    const {ownerSeq} = useParams();

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

    const checkEmail = () => {
        if (!isEmailCheck) {
            if (email !== "" && !isEmail(email)) {
                window.alert("이메일 형식이 맞지 않습니다.\n다시 입력해주세요.");
            }
        }
    }

    const sendStore = async () => {
        let error = false;
        let message = "";

        if (email === "" && !error) {
            error = true;
            message = "이메일 정보가 없습니다.";
        }
        if (birth === "" && !error) {
            error = true;
            message = "생일 정보가 없습니다.";
        }
        if (name === "" && !error) {
            error = true;
            message = "성명 정보가 없습니다.";
        }
        if (password === "" && !error && !ownerSeq) {
            error = true;
            message = "비밀번호 정보가 없습니다.";
        }
        if (password2 === "" && !error && !ownerSeq) {
            error = true;
            message = "비밀번호 확인 정보가 없습니다.";
        }
        if (password2 !== password && !error) {
            error = true;
            message = "비밀번호 정보가 일치하지 않습니다.";
        }

        if (!error) {
            let arr = stores;

            //file upload
            for (let j = 0; j < arr.length; j++) {
                if (arr[j].sname === "" && !error) {
                    error = true;
                    message = "상점명 정보가 없습니다.";
                }
                if (arr[j].sinfo === "" && !error) {
                    error = true;
                    message = "상점 상세정보가 없습니다.";
                }

                if (arr[j].addr1 === "" && !error) {
                    error = true;
                    message = "상점 주소정보가 없습니다.";
                }

                for (let i = 0; i < arr[j].files.length; i++) {
                    if (i < 3) {
                        if (arr[j].files[i].fileinfo != null && !error) {
                            let formData = new FormData();
                            formData.append("file", arr[j].files[i].fileinfo)
                            try {
                                const response = await axios.post("/file/upload", formData, {
                                    headers: {
                                        "Content-Type": "multipart/form-data",
                                        "mint-token": localStorage.getItem("mint-token")
                                    }
                                });

                                const data = response.data;

                                console.log(data);
                                if (data.status === 200) {
                                    console.log("file save");
                                    arr[j].files[i].seq = data.result.seq;
                                    arr[j].files[i].url = data.result.url;
                                    arr[j].files[i].type = 1;
                                    arr[j].files[i].fileinfo = null;
                                } else {
                                    error = true;
                                    message = "파일 업로드 중 에러가 발생되었습니다.";
                                    break;
                                }

                            } catch (error) {
                                console.log(error.message);
                                error = true;
                                message = "파일 업로드 중 에러가 발생되었습니다.";
                                break;
                            }
                        } else if (!arr[j].files[i].url) {
                            arr[j].files.splice(i, 1);
                        }
                    } else {
                        arr[j].files.splice(i, 1);
                    }
                }
            }

            if (ownerSeq) {

                try {
                    const response2 = await axios.post("/api/member/edit", {
                        seq: ownerSeq,
                        email: email,
                        birth: birth.toISOString().substring(0, 10),
                        name: name,
                        nik: nik,
                        pass: (password ? password : null),
                        sex: sex,
                        job: job,
                        level: level,
                        status: status,
                        type: 2,
                        store: arr
                    }, {
                        headers: {
                            "Content-Type": "application/json",
                            "mint-token": localStorage.getItem("mint-token")
                        }
                    });

                    const data2 = response2.data;
                    console.log(data2);

                    if (data2.status === 200) {
                        alert("상점주가 수정되었습니다.");
                    } else {
                        error = true;
                        message = "상점주 수정중 에러가 발생하였습니다.";
                    }
                } catch (error) {
                    console.log(error.message);
                    error = true;
                    message = "상점주 수정중 에러가 발생하였습니다.";
                }
            } else {
                try {
                    const response2 = await axios.post("/api/member/add", {
                        email: email,
                        birth: birth.toISOString().substring(0, 10),
                        name: name,
                        nik: nik,
                        pass: password,
                        sex: sex,
                        job: job,
                        level: level,
                        status: status,
                        type: 2,
                        store: arr
                    }, {
                        headers: {
                            "Content-Type": "application/json",
                            "mint-token": localStorage.getItem("mint-token")
                        }
                    });

                    const data2 = response2.data;
                    console.log(data2);

                    if (data2.status === 200) {
                        alert("상점주가 등록되었습니다.\n목록으로 이동합니다.");
                        navigate("/uhsa/owner?pg=" + pg + "&st=" + encodeURIComponent(st));
                    } else {
                        error = true;
                        message = "상점주 등록중 에러가 발생하였습니다.";
                    }
                } catch (error) {
                    console.log(error.message);
                    error = true;
                    message = "상점주 등록중 에러가 발생하였습니다.";
                }
            }
        }

        if (error) {
            alert(message);
        }
    }

    const gotoBack = () => {
        navigate("/uhsa/owner?pg=" + pg + "&st=" + encodeURIComponent(st));
    }

    const callback = (data) => {
        setStores(data);
    }

    registerLocale('ko', ko);

    const loadStore = async () => {
        let error = false;
        let message = "";
        try {
            const response = await axios.post("/api/member/info", {
                seq: ownerSeq
            }, {
                headers: {
                    "Content-Type": "application/json",
                    "mint-token": localStorage.getItem("mint-token")
                }
            });

            console.log(ownerSeq, response);

            const data = response.data;

            if (data.status === 200) {
                setEmail(data.result.email);
                setBirth(new Date(data.result.birth));
                setName(data.result.name);
                setNik(data.result.nik);
                setSex(data.result.sex);
                setJob(data.result.job);
                setLevel(data.result.level);
                setStatus(data.result.status);
            } else {
                error = true;
                message = "상점주 정보를 읽어오는 동안 오류가 발생하였습니다.";
            }

        } catch (e) {
            console.log(e.message);
            error = true;
            message = "상점주 정보를 읽어오는 동안 오류가 발생하였습니다.";
        }

        if (error) {
            alert(message);
        }
    }

    useEffect(() => {
        console.log(ownerSeq);
        if (ownerSeq) {
            loadStore();
        }
    }, []);

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
                        </CRow>
                    </CCardTitle>
                    <CRow className="ps-2 pe-2">
                        <OwnerStore sdata={stores} calluse={callback}/>
                    </CRow>
                </CCardBody>
            </CCard>
            <div className="text-center p-5 d-md-block">
                <CButton color="dark" onClick={gotoBack}>목록으로</CButton>
                <CButton className="ms-3" onClick={sendStore}><CIcon
                    icon={cilUserPlus}/> {ownerSeq ? "상점주 수정하기" : "상점주 새로등록"}</CButton>
            </div>
        </div>
    )
}

export default OwnerAdd