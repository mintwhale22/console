import React, {useRef, useState} from 'react'
import {
    CButton,
    CCardText,
    CCol,
    CFormInput,
    CFormSelect, CFormTextarea,
    CInputGroup,
    CInputGroupText, CModal, CModalBody, CModalHeader, CModalTitle,
    CRow, CTooltip
} from "@coreui/react";
import CIcon from "@coreui/icons-react";
import {cilHouse, cilSearch, cilTrash} from "@coreui/icons";
import noimage from './../../assets/images/noimage.png';
import {isImage} from "../../utils/Regexs";
import DaumPostcodeEmbed from "react-daum-postcode";
import {AddrtoGps} from "../../utils/AddrtoGps";

const OwnerStore = ({sdata, calluse}) => {

    const [arrData, setArrData] = useState(sdata.length === 0 ? [] : sdata);
    const [last, setLast] = useState(-1);
    const ref = useRef({});
    const [visible, setVisible] = useState(false);
    const [dindex, setDindex] = useState(-1);

    const addStore = () => {
        setLast(last - 1);
        setArrData(arrData => [...arrData, {
            seq: last,
            sname: "",
            sinfo: "",
            status: 1,
            zipcode: "",
            addr1: "",
            addr2: "",
            lat: 0.0,
            lng: 0.0,
            files: [{filename: "", fileinfo: null}, {filename: "", fileinfo: null}, {filename: "", fileinfo: null}]
        }]);
        calluse(arrData);
    }

    const setData = (index, type) => e => {
        let arr = [...arrData];
        switch (type) {
            case 1 :
                arr[index].sname = e.target.value;
                break;
            case 2 :
                arr[index].status = e.target.value;
                break;
            case 3 :
                arr[index].sinfo = e.target.value;
                break;
            case 4 :
                arr[index].zipcode = e.target.value;
                break;
            case 5 :
                arr[index].addr1 = e.target.value;
                break;
            case 6 :
                arr[index].addr2 = e.target.value;
                break;
            case 7 :
                arr[index].lat = e.target.value;
                break;
            case 8 :
                arr[index].lng = e.target.value;
                break;
        }

        setArrData(arr);
        calluse(arrData);
    }

    const fileChange = (index, count) => e => {
        console.log(e.target.files[0]);
        if (!isImage(e.target.files[0].name)) {
            alert("이미지 파일이 아닙니다.");
        } else {
            let arr = [...arrData];
            let check = false;

            for (let i = 0; i < arr[index].files.length; i++) {
                if (arr[index].files[i].fileinfo != null && arr[index].files[i].fileinfo.name === e.target.files[0].name && arr[index].files[i].fileinfo.size === e.target.files[0].size) {
                    check = true;
                    break;
                }
            }

            if (check) {
                alert("이미 선택한 파일입니다.");
            } else {
                arr[index].files[count].fileinfo = e.target.files[0];
                arr[index].files[count].filename = URL.createObjectURL(e.target.files[0]);

                setArrData(arr);
                calluse(arrData);
            }
        }
    };

    const fileUnselect = (index, count) => {
        let arr = [...arrData];

        arr[index].files[count].fileinfo = null;
        arr[index].files[count].filename = '';

        setArrData(arr);
        calluse(arrData);
    }

    const delData = (seq) => {
        console.log(seq);
        setArrData(arrData.filter(store => store.seq !== seq));
        calluse(arrData);
    }

    const showAddr = (index) => {
        setDindex(index);
        setVisible(true);
    }

    const selectAddr = (data) => {
        console.log(dindex, data);

        let arr = [...arrData];

        let fullAddress = data.address;
        let extraAddress = '';

        if (data.addressType === 'R') {
            if (data.bname !== '') {
                extraAddress += data.bname;
            }
            if (data.buildingName !== '') {
                extraAddress += extraAddress !== '' ? `, ${data.buildingName}` : data.buildingName;
            }

            fullAddress += extraAddress !== '' ? ` (${extraAddress})` : '';
        }

        AddrtoGps(data.address, (data) => {
            console.log(data);

            if(data != null) {
                arr[dindex].lat = data.lat;
                arr[dindex].lng = data.lng;
            }
        })

        arr[dindex].addr1 = fullAddress;
        arr[dindex].addr2 = "";
        arr[dindex].zipcode = data.zonecode;

        setArrData(arr);
        calluse(arrData);
        setDindex(-1);
        setVisible(false);
    }

    return (
        <React.Fragment>
            {arrData.length === 0 ? (
                <CRow className="p-3 text-center">
                    <CCardText>상점정보가 없습니다.</CCardText>
                </CRow>
            ) : (
                arrData.map((store, index) => (
                        <div className="mt-3">
                            <hr className={(index > 0 ? '' : 'hidden') + " mb-5"}/>
                            <CRow>
                                <CCol className="col-7">
                                    <CInputGroup>
                                        <CInputGroupText id={"basic-store" + index + "-addon1"}
                                                         className="col-3">상점명</CInputGroupText>
                                        <CFormInput placeholder="상점명을 입력해주세요."
                                                    onChange={setData(index, 1)}
                                                    value={store.sname}/>
                                    </CInputGroup>
                                </CCol>
                                <CCol>
                                    <CInputGroup>
                                        <CInputGroupText id={"basic-store" + index + "-addon2"}
                                                         className="col-4">상태</CInputGroupText>
                                        <div className="form-control">
                                            <CFormSelect aria-label={"basic-store" + index + "-addon2"}
                                                         className="border-0 p-0"
                                                         onChange={setData(index, 2)}>
                                                <option value="1" selected={store.status === 1}>사용</option>
                                                <option value="9" selected={store.status === 9}>사용안함</option>
                                            </CFormSelect>
                                        </div>
                                    </CInputGroup>
                                </CCol>
                                <CCol className="col-1 text-end">
                                    <CButton color="dark" variant="outline" onClick={() => {
                                        delData(store.seq);
                                    }}><CIcon icon={cilTrash}/></CButton>
                                </CCol>
                            </CRow>
                            <CRow className="mt-3">
                                <CCol className="col-7">
                                    <CInputGroup>
                                        <CInputGroupText id={"basic-store" + index + "-addon8"}
                                                         className="col-3">우편번호</CInputGroupText>
                                        <CFormInput placeholder="" disabled={true}
                                                    onChange={setData(index, 4)}
                                                    value={store.zipcode}/>
                                    </CInputGroup>
                                </CCol>
                                <CCol>
                                    <CButton color="dark" variant="outline" onClick={() => {
                                        showAddr(index)
                                    }}><CIcon icon={cilSearch}/> 주소검색</CButton>
                                </CCol>
                            </CRow>
                            <CRow className="mt-3">
                                <CCol className="col-7">
                                    <CInputGroup>
                                        <CInputGroupText id={"basic-store" + index + "-addon9"}
                                                         className="col-3">주소</CInputGroupText>
                                        <CFormInput placeholder="주소를 검색해주세요." disabled={true}
                                                    onChange={setData(index, 5)}
                                                    value={store.addr1} className="col-6"/>
                                    </CInputGroup>
                                </CCol>
                                <CCol>
                                    <CInputGroup>
                                        <CFormInput placeholder="상세주소를 입력해주세요."
                                                    onChange={setData(index, 6)}
                                                    value={store.addr2} className="col-5"/>
                                    </CInputGroup>
                                </CCol>
                            </CRow>
                            <CRow className="mt-3">
                                <CCol className="text-center yh-relative">
                                    <CTooltip
                                        content={(store.files.length > 0 && store.files[0].filename ? "이미지를 변경하시려면 클릭해주세요." : "이미지를 선택해주세요.")}
                                        placement="top">
                                        <img
                                            src={(store.files.length > 0 && store.files[0].filename ? store.files[0].filename : noimage)}
                                            className="yh-w200 yh-h200 yh-pointer"
                                            onClick={() => ref.current[index + 100].click()}/>
                                    </CTooltip>
                                    <CFormInput type="file" ref={element => (ref.current[index + 100] = element)}
                                                hidden={true} multiple={false} onChange={fileChange(index, 0)}/>
                                    <CButton color="light" size="sm" shape="rounded-pill" onClick={() => {
                                        fileUnselect(index, 0)
                                    }}
                                             className={(store.files[0].filename !== "" ? '' : 'hidden') + " m-1 yh-absolute yh-right yh-top"}><CIcon
                                        icon={cilTrash}/></CButton>
                                </CCol>
                                <CCol className="text-center yh-relative">
                                    <CTooltip
                                        content={(store.files.length > 1 && store.files[1].filename ? "이미지를 변경하시려면 클릭해주세요." : "이미지를 선택해주세요.")}
                                        placement="top">
                                        <img
                                            src={(store.files.length > 1 && store.files[1].filename ? store.files[1].filename : noimage)}
                                            className="yh-w200 yh-h200 yh-pointer"
                                            onClick={() => ref.current[index + 200].click()}/>
                                    </CTooltip>
                                    <CFormInput type="file" ref={element => (ref.current[index + 200] = element)}
                                                hidden={true} multiple={false} onChange={fileChange(index, 1)}/>
                                    <CButton color="light" size="sm" shape="rounded-pill" onClick={() => {
                                        fileUnselect(index, 1)
                                    }}
                                             className={(store.files[1].filename !== "" ? '' : 'hidden') + " m-1 yh-absolute yh-right yh-top"}><CIcon
                                        icon={cilTrash}/></CButton>
                                </CCol>
                                <CCol className="text-center yh-relative">
                                    <CTooltip
                                        content={(store.files.length > 2 && store.files[2].filename ? "이미지를 변경하시려면 클릭해주세요." : "이미지를 선택해주세요.")}
                                        placement="top">
                                        <img
                                            src={(store.files.length > 2 && store.files[2].filename ? store.files[2].filename : noimage)}
                                            className="yh-w200 yh-h200 yh-pointer"
                                            onClick={() => ref.current[index + 300].click()}/>
                                    </CTooltip>
                                    <CFormInput type="file" ref={element => (ref.current[index + 300] = element)}
                                                hidden={true} multiple={false} onChange={fileChange(index, 2)}/>
                                    <CButton color="light" size="sm" shape="rounded-pill" onClick={() => {
                                        fileUnselect(index, 2)
                                    }}
                                             className={(store.files[2].filename !== "" ? '' : 'hidden') + " m-1 yh-absolute yh-right yh-top"}><CIcon
                                        icon={cilTrash}/></CButton>
                                </CCol>
                            </CRow>
                            <CRow className="p-3">
                                <CFormTextarea id={"basic-store" + index + "-addon3"} rows="5"
                                               onChange={setData(index, 3)}>{store.sinfo}</CFormTextarea>
                            </CRow>
                        </div>
                    )
                )
            )}
            <CRow className="pt-3 text-center">
                <CButton onClick={addStore}><CIcon icon={cilHouse}/> 상점추가</CButton>
            </CRow>

            <CModal alignment="center" visible={visible} onClose={() => setVisible(false)}>
                <CModalHeader>
                    <CModalTitle>주소검색</CModalTitle>
                </CModalHeader>
                <CModalBody>
                    <DaumPostcodeEmbed onComplete={selectAddr} autoClose={false}/>;
                </CModalBody>
            </CModal>
        </React.Fragment>
    );

}
export default OwnerStore