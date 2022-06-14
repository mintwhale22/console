import React, {useState} from 'react'
import {CCard, CCardBody, CCardText, CCol, CFormSelect, CInputGroup, CInputGroupText, CRow} from "@coreui/react";


let array = [];

export const getData = () => {
    return array;
}

const setData = (index, type, data) => {

}

const OwnerStore = (props) => {

    if (props.data.length === 0) {
        return (
            <CCardText className="p-3 text-center">빈값</CCardText>
        );
    } else {
        array = props.data;

        return (
            props.data.map((store, index) => (
                    <CCard>
                        <CCardBody>
                            <CRow>
                                <CCol className="col-4">
                                    <span>대표이미지</span>

                                </CCol>
                                <CCol className="col-8">
                                    <CRow>
                                    <CInputGroup>
                                        <CInputGroupText id={"basic-store"+ index +"-addon1"} className="col-4">상태</CInputGroupText>
                                        <div className="form-control">
                                            <CFormSelect aria-label="status" className="border-0 p-0">
                                                <option value="1" selected={store.status === 1}>사용</option>
                                                <option value="9" selected={store.status === 9}>사용안함</option>
                                            </CFormSelect>
                                        </div>
                                    </CInputGroup>
                                    </CRow>
                                </CCol>
                            </CRow>
                        </CCardBody>
                    </CCard>
                )
            )
        );
    }
}
export default OwnerStore