import React from 'react'

const Dashboard = React.lazy(() => import('./pages/dashboard/Dashboard'))
const Profile = React.lazy( () => import('./pages/profile/Profile'))
const Owner = React.lazy( () => import('./pages/owner/Owner'))
const Users = React.lazy( () => import('./pages/users/Users'))

const routes = [
  { path: '/', exact: true, name: '메인' },
  { path: '/dashboard', name: '대시보드', element: Dashboard },
  { path: '/profile', name: '내정보', element: Profile },
  { path: '/owner', name: '상점주관리', element: Owner },
  { path: '/users', name: '사용자관리', element: Users },
]

export default routes
