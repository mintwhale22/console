import React, {useState} from 'react'
import {
    CButton,
    CCard,
    CCardBody,
    CCardText,
    CCol,
    CFormInput,
    CFormSelect, CFormTextarea,
    CInputGroup,
    CInputGroupText,
    CRow
} from "@coreui/react";


let array = [];

export const getData = () => {
    return array;
}

const setData = (index, type, data) => {
    switch (type) {
        case 1:
            array[index].sname = data;
            break;
        case 2:
            array[index].status = data;
            break;
        case 3:
            array[index].sinfo = data;
            break;
    }

}

const OwnerStore = (props) => {

    if (props.data.length === 0) {
        return (
            <CCardText className="p-3 text-center">상점정보가 없습니다.</CCardText>
        );
    } else {
        array = props.data;

        return (
            props.data.map((store, index) => (
                    <CCard className="mt-3">
                        <CCardBody>
                            <CRow className="p-3">
                                <CCol>

                                </CCol>
                                <CCol>
                                </CCol>
                                <CCol>
                                </CCol>
                                <CCol>
                                    
                                </CCol>
                            </CRow>
                            <CRow>
                                <CCol>
                                    <CInputGroup>
                                        <CInputGroupText id={"basic-store" + index + "-addon1"}
                                                         className="col-4">상점명</CInputGroupText>
                                        <CFormInput placeholder="상점명을 입력해주세요." aria-label={"basic-store" + index + "-addon2"}
                                                    aria-describedby={"basic-store" + index + "-addon1"}
                                                    onChange={({target: {value}}) => setData(index, 1, value)}
                                                    value={store.sname}/>
                                    </CInputGroup>
                                </CCol>
                                <CCol>
                                    <CInputGroup>
                                        <CInputGroupText id={"basic-store" + index + "-addon2"}
                                                         className="col-4">상태</CInputGroupText>
                                        <div className="form-control">
                                            <CFormSelect aria-label={"basic-store" + index + "-addon2"} className="border-0 p-0"
                                                         onChange={({target: {value}}) => setData(index, 2, value)}>
                                                <option value="1" selected={store.status === 1}>사용</option>
                                                <option value="9" selected={store.status === 9}>사용안함</option>
                                            </CFormSelect>
                                        </div>
                                    </CInputGroup>
                                </CCol>
                            </CRow>
                            <CRow className="p-3">
                                <CFormTextarea id={"basic-store" + index + "-addon3"} rows="5" onChange={({target: {value}}) => setData(index, 3, value)}>{store.sinfo}</CFormTextarea>
                            </CRow>
                        </CCardBody>
                    </CCard>
                )
            )
        );
    }
}
export default OwnerStore