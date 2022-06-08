import React, {useEffect, useState} from 'react'
import {
    CAvatar,
    CBadge,
    CDropdown,
    CDropdownDivider,
    CDropdownHeader,
    CDropdownItem,
    CDropdownMenu,
    CDropdownToggle,
} from '@coreui/react'
import {
    cilBell,
    cilCreditCard,
    cilCommentSquare,
    cilEnvelopeOpen,
    cilFile,
    cilLockLocked,
    cilSettings,
    cilTask,
    cilUser,
} from '@coreui/icons'
import CIcon from '@coreui/icons-react'

import avatar8 from './../../assets/images/avatars/8.jpg'
import axios from "axios";

const setLogout = () => {
    localStorage.removeItem("mint-token");
    localStorage.removeItem("mint-info");
    window.location.reload();
}

const AppHeaderDropdown = () => {
    const [name, setName] = useState("");
    useEffect(() => {
        const headers = {
            "mint-token": localStorage.getItem("mint-token"),
            'Context-Type': "application/json"
        }

        axios.get("/api/info", { headers }).then(response => {
            const data = response.data;
            if (data.status === 200) {
                if(data.result.nik) {
                    localStorage.setItem("mint-info", JSON.stringify(data.result));
                    setName(data.result.nik);
                } else {
                    setName(data.result.name);
                }
            } else if (data.status >= 600 && data.status < 700) {
                alert(data.message);
                setLogout();
            }
        }).catch(error => {
            console.log(error.message);
            if (window.confirm("회원정보를 가져올 수 없습니다.\n다시 로드 하시겠습니까?")) {
                this.refresh();
            }
        })
    }, []);

    return (
        <CDropdown variant="nav-item">
            <CDropdownToggle placement="bottom-end" className="py-0" caret={false}>
                <CAvatar src={avatar8} size="md"/>
            </CDropdownToggle>
            <CDropdownMenu className="pt-0" placement="bottom-end">
                <CDropdownHeader className="bg-light fw-semibold py-2">활동</CDropdownHeader>
                <CDropdownItem href="#">
                    <CIcon icon={cilBell} className="me-2"/>
                    알림
                    <CBadge color="info" className="ms-2">
                        42
                    </CBadge>
                </CDropdownItem>
                <CDropdownItem href="#">
                    <CIcon icon={cilTask} className="me-2"/>
                    활동내역
                    <CBadge color="danger" className="ms-2">
                        42
                    </CBadge>
                </CDropdownItem>
                <CDropdownHeader className="bg-light fw-semibold py-2">설정</CDropdownHeader>
                <CDropdownItem href="#">
                    <CIcon icon={cilUser} className="me-2"/>
                    {name} 정보
                </CDropdownItem>
                <CDropdownItem href="#">
                    <CIcon icon={cilSettings} className="me-2"/>
                    사이트 설정
                </CDropdownItem>
                <CDropdownDivider/>
                <CDropdownItem href="#" onClick={setLogout}>
                    <CIcon icon={cilLockLocked} className="me-2"/>
                    로그아웃
                </CDropdownItem>
            </CDropdownMenu>
        </CDropdown>
    )
}

export default AppHeaderDropdown
