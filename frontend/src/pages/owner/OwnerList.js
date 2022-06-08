import React, {useState} from 'react'
import {CButton, CCol, CContainer, CFormInput, CInputGroup, CRow} from "@coreui/react";
import CIcon from "@coreui/icons-react";
import {cilSearch, cilUserPlus} from "@coreui/icons";
import {useNavigate} from "react-router-dom";
import axios from "axios";

const OwnerList = () => {

    const navigate = useNavigate()

    const {searchtext, setSearchtext } = useState("")

    const restPut = {
        stext : "",
        limit : 0,
        length : 20
    }

    const loadData = async () => {
        console.log(restPut);
        axios.put("/api/member/list")
    }

    const setSearch = () => {
        restPut.stext = searchtext ? searchtext : "";
        restPut.limit = 0;
        loadData();
    }

    const gotoAdd = () => {
        navigate("/owner/add");
    }

    return (
        <CContainer>
            <CRow>
                <CCol xs={9}>
                    <CInputGroup className="mb-3">
                        <CFormInput placeholder="검색어를 입력해주세요." aria-label="검색어를 입력해주세요." aria-describedby="button-addon2" onChange={({target: {value}}) => setSearchtext(value)} value={searchtext}/>
                        <CButton type="button" variant="outline" id="button-addon2" onClick={setSearch}><CIcon icon={cilSearch} /> 검색</CButton>
                    </CInputGroup>

                </CCol>
                <CCol className="text-end">
                    <CButton onClick={gotoAdd} variant="outline"><CIcon icon={cilUserPlus}/> 새로등록</CButton>
                </CCol>
            </CRow>
        </CContainer>
    )
}

export default OwnerList