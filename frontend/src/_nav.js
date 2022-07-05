import React from 'react'
import CIcon from '@coreui/icons-react'
import {
  cilBell,
  cilCalculator,
  cilChartPie,
  cilCursor,
  cilDrop,
  cilNotes,
  cilPencil,
  cilPuzzle,
  cilSpeedometer,
  cilStar, cilUser,
} from '@coreui/icons'
import {CImage, CNavGroup, CNavItem, CNavTitle} from '@coreui/react'
import uhsa_logo from "src/assets/images/uhsa_logo.png"

const _nav = [
  {
    component: CNavItem,
    name: '대시보드',
    to: '/dashboard',
    icon: <CIcon icon={cilSpeedometer} customClassName="nav-icon" />,
    badge: {
      color: 'info',
      text: 'NEW',
    },
  },
  {
    component: CNavGroup,
    name: '할인어사',
    icon: <CImage src={uhsa_logo} customClassName="nav-icon" className="yh-navi-logo" width={20} height={20} />,
    items: [
      {
        component: CNavItem,
        name: '앱설정',
        to: '/uhsa/appset',
      },
      {
        component: CNavItem,
        name: '공지사항',
        to: '/uhsa/notice',
      },
      {
        component: CNavItem,
        name: '상점주관리',
        to: '/uhsa/owner',
      },
      {
        component: CNavItem,
        name: '사용자관리',
        to: '/uhsa/users',
      },
    ],
  },
]

export default _nav
