import React, {useEffect, useState} from 'react'
import {
    CButton,
    CCol,
    CContainer,
    CFormInput,
    CInputGroup, CPagination, CPaginationItem,
    CRow, CSpinner,
    CTable,
    CTableBody, CTableDataCell,
    CTableHead, CTableHeaderCell,
    CTableRow
} from "@coreui/react";
import CIcon from "@coreui/icons-react";
import {
    cilMediaSkipBackward,
    cilMediaSkipForward,
    cilMediaStepBackward,
    cilMediaStepForward,
    cilSearch,
    cilUserPlus
} from "@coreui/icons";
import {useNavigate} from "react-router-dom";
import axios from "axios";
import queryString from 'query-string';

const UserList = () => {
    let search = window.location.search ? window.location.search : window.location.hash;
    search = search.indexOf("?") > -1 ? search.split("?")[1] : "";
    const {pg, st} = queryString.parse(search);

    const navigate = useNavigate();

    const [searchtext, setSearchtext] = useState(st ? st : "");
    const [length, setLength] = useState(15);
    const [order, setOrther] = useState(1);
    const [page, setPage] = useState(pg ? pg : 1);
    const [totalcount, setTotalcount] = useState(0);
    const [ypaging, setYpaging] = useState({
        max: 0,
        prev: 0,
        next: 0,
        start: 0,
        end: 0
    });
    const [loading, setLoading] = useState(false);
    const pageLangth = 10;

    const [list, setList] = useState([]);

    const loadData = async (gopage = page) => {
        let error = false;
        let message = "";
        let nowpage = ypaging;

        setLoading(true);
        try {
            const response2 = await axios.post("/api/member/list", {
                stext: searchtext,
                limit: (gopage - 1) * length,
                length: length,
                order: order,
                search: {
                    "type": 1
                }
            }, {
                headers: {
                    "Content-Type": "application/json",
                    "mint-token": localStorage.getItem("mint-token")
                }
            });

            const data = response2.data;
            console.log(data);

            if (data.status === 200) {
                setList(data.result.list);
                //pageing count
                setTotalcount(data.result.totalcount);
                nowpage.max = Math.ceil(data.result.totalcount / length);

                if (nowpage.max < pageLangth) {
                    nowpage.start = 1;
                    nowpage.end = nowpage.max;
                    nowpage.prev = 1;
                    nowpage.next = nowpage.max;
                } else {
                    nowpage.start = (gopage - Math.floor(pageLangth / 2)) < 1 ? 1 : (gopage - Math.floor(pageLangth / 2));
                    nowpage.end = nowpage.max < (nowpage.start + pageLangth) ? nowpage.max : nowpage.start + pageLangth;
                    if (nowpage.end === nowpage.max) {
                        nowpage.start = nowpage.max - (pageLangth - 1);
                    }
                    nowpage.prev = (gopage - pageLangth) < 1 ? 1 : (gopage - pageLangth);
                    nowpage.next = (gopage + pageLangth) > nowpage.max ? nowpage.max : (gopage + pageLangth);
                }
                console.log(nowpage);
                setYpaging(nowpage);
            } else {
                error = true;
                message = "사용자 목록을 읽어오는 동안 오류가 발생하였습니다.";
            }
            setLoading(false);
        } catch (error) {
            console.log(error.message);
            error = true;
            message = "사용자 목록을 읽어오는 동안 오류가 발생하였습니다.";
            setLoading(false);
        }

        if (error) {
            alert(message);
        }

    }

    const setSearch = () => {
        loadData();
    }

    const gotoEdit = (seq) => {
        navigate("/uhsa/users/edit/" + seq.toString() + "?pg=" + page + "&st=" + encodeURIComponent(searchtext.toString()));
    }

    const gotoAdd = () => {
        navigate("/uhsa/users/add?pg=" + page + "&st=" + encodeURIComponent(searchtext.toString()));
    }

    const gotoPage = (gopage) => {
        setPage(gopage);
        loadData(gopage);
    }

    useEffect(() => {
        loadData();
    }, []);

    return (
        <CContainer>
            <CRow>
                <CCol xs={9}>
                    <CInputGroup className="mb-3">
                        <CFormInput placeholder="검색어를 입력해주세요." aria-label="검색어를 입력해주세요."
                                    aria-describedby="button-addon2"
                                    onChange={({target: {value}}) => setSearchtext(value)} value={searchtext}/>
                        <CButton type="button" id="button-addon2" onClick={setSearch}><CIcon
                            icon={cilSearch}/> 검색</CButton>
                    </CInputGroup>
                </CCol>
                <CCol className="text-end">
                    <CButton onClick={gotoAdd}><CIcon icon={cilUserPlus}/> 새로등록</CButton>
                </CCol>
            </CRow>
            <CRow className="mt-3">
                <CTable>
                    <CTableHead color="light">
                        <CTableRow>
                            <CTableHeaderCell scope="col" className="text-center">번호</CTableHeaderCell>
                            <CTableHeaderCell scope="col" className="text-center">사용자명</CTableHeaderCell>
                            <CTableHeaderCell scope="col" className="text-center">이메일</CTableHeaderCell>
                            <CTableHeaderCell scope="col" className="text-center">성별</CTableHeaderCell>
                            <CTableHeaderCell scope="col" className="text-center">레벨</CTableHeaderCell>
                            <CTableHeaderCell scope="col" className="text-center">상태</CTableHeaderCell>
                            <CTableHeaderCell scope="col" className="text-center">제어</CTableHeaderCell>
                        </CTableRow>
                    </CTableHead>
                    <CTableBody>
                        {
                            loading === true ? (
                                <CTableRow>
                                    <CTableDataCell colSpan={7} className="p-5 text-center"><CSpinner
                                        color="info"/><br/>로딩중...</CTableDataCell>
                                </CTableRow>
                            ) : (list.length === 0 ? (
                                    <CTableRow>
                                        <CTableDataCell colSpan={7} className="p-5 text-center">사용자 정보가
                                            없습니다.</CTableDataCell>
                                    </CTableRow>
                                ) : (
                                    list.map((member, index) => (
                                        <CTableRow valign="middle">
                                            <CTableDataCell
                                                scope="row"
                                                className="text-center">{totalcount - ((page - 1) * length + index)}</CTableDataCell>
                                            <CTableDataCell className="text-center">{member.name}<br/>{member.nik ? (
                                                <span>{"(" + member.nik + ")"}</span>) : ""}</CTableDataCell>
                                            <CTableDataCell className="text-center">{member.email}</CTableDataCell>
                                            <CTableDataCell
                                                className="text-center">{member.sex === 1 ? "남자" : (member.sex === 2 ? "여자" : "무관")}</CTableDataCell>
                                            <CTableDataCell className="text-center">{member.level}레벨</CTableDataCell>
                                            <CTableDataCell
                                                className="text-center">{member.status === 1 ? "정상" : (member.status === 5 ? "차단" : "탈퇴")}</CTableDataCell>
                                            <CTableDataCell className="text-center">
                                                <CButton onClick={() => {
                                                    gotoEdit(member.seq)
                                                }}>수정</CButton>
                                            </CTableDataCell>
                                        </CTableRow>
                                    ))
                                )
                            )
                        }
                    </CTableBody>
                </CTable>
            </CRow>
            <CRow>
                {
                    loading !== true && (
                        <CPagination align="center">
                            {
                                ypaging.max > pageLangth && (
                                    <CPaginationItem className="yh-pointer" onClick={() => {
                                        gotoPage(1)
                                    }}>
                                        <CIcon icon={cilMediaStepBackward}/>
                                    </CPaginationItem>
                                )
                            }
                            {
                                ypaging.max > pageLangth && (
                                    <CPaginationItem className="yh-pointer" onClick={() => {
                                        gotoPage(ypaging.prev)
                                    }} disabled={page <= pageLangth}>
                                        <CIcon icon={cilMediaSkipBackward}/>
                                    </CPaginationItem>
                                )
                            }
                            {
                                Array((ypaging.end - ypaging.start + 1)).fill().map((_, index) => (
                                    <CPaginationItem className="yh-pointer" onClick={() => {
                                        gotoPage(ypaging.start + index)
                                    }} active={(ypaging.start + index).toString() === page.toString()}>
                                        {ypaging.start + index}
                                    </CPaginationItem>
                                ))
                            }
                            {
                                ypaging.max > pageLangth && (
                                    <CPaginationItem className="yh-pointer" onClick={() => {
                                        gotoPage(ypaging.next)
                                    }} disabled={page >= (ypaging.max - pageLangth)}>
                                        <CIcon icon={cilMediaSkipForward}/>
                                    </CPaginationItem>
                                )
                            }
                            {
                                ypaging.max > pageLangth && (
                                    <CPaginationItem className="yh-pointer" onClick={() => {
                                        gotoPage(ypaging.max)
                                    }}>
                                        <CIcon icon={cilMediaStepForward}/>
                                    </CPaginationItem>
                                )
                            }
                        </CPagination>
                    )
                }
            </CRow>
        </CContainer>
    )
}

export default UserList