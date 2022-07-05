import React from 'react'

const Dashboard = React.lazy(() => import('./pages/dashboard/Dashboard'))
const Profile = React.lazy(() => import('./pages/profile/Profile'))
const OwnerList = React.lazy(() => import('./pages/uhsa/owner/OwnerList'))
const OwnerAdd = React.lazy(() => import('./pages/uhsa/owner/OwnerAdd'))
const UsersList = React.lazy(() => import('./pages/uhsa/users/UsersList'))
const UsersAdd = React.lazy(() => import('./pages/uhsa/users/UsersAdd'))

const routes = [
    {path: '/', exact: true, name: '메인'},
    {path: '/dashboard', name: '대시보드', element: Dashboard},
    {path: '/profile', name: '내정보', element: Profile},
    {path: '/uhsa/owner', name: '상점주관리', element: OwnerList},
    {path: '/uhsa/owner/add', name: '상점주 등록', element: OwnerAdd},
    {path: '/uhsa/owner/edit', name: '상점주 수정', element: OwnerAdd},
    {path: '/uhsa/owner/edit/:ownerSeq', name: '상점주 수정', element: OwnerAdd},
    {path: '/uhsa/users', name: '사용자관리', element: UsersList},
    {path: '/uhsa/users/add', name: '사용자 등록', element: UsersAdd},
    {path: '/uhsa/users/edit', name: '사용자 수정', element: UsersAdd},
    {path: '/uhsa/users/edit/:userSeq', name: '사용자 수정', element: UsersAdd},
]

export default routes
