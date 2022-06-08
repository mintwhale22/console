import React from 'react'

const Dashboard = React.lazy(() => import('./pages/dashboard/Dashboard'))
const Profile = React.lazy(() => import('./pages/profile/Profile'))
const OwnerList = React.lazy(() => import('./pages/owner/OwnerList'))
const OwnerAdd = React.lazy(() => import('./pages/owner/OwnerAdd'))
const Users = React.lazy(() => import('./pages/users/Users'))

const routes = [
    {path: '/', exact: true, name: '메인'},
    {path: '/dashboard', name: '대시보드', element: Dashboard},
    {path: '/profile', name: '내정보', element: Profile},
    {path: '/owner', name: '상점주관리', element: OwnerList},
    {path: '/owner/add', name: '새로등록', element: OwnerAdd},
    {path: '/owner/edit/:seq', name: '수정', element: OwnerAdd},
    {path: '/users', name: '사용자관리', element: Users},
]

export default routes
