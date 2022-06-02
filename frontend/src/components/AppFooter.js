import React from 'react'
import { CFooter } from '@coreui/react'

const AppFooter = () => {
  return (
    <CFooter>
      <div style={{textAlign: 'center', display: 'block'}}>
        <a href="https://www.mintwhale.kr" target="_blank" rel="noopener noreferrer">
          MintWhale.kr
        </a>
        <span className="ms-1">&copy; 2022.</span>
      </div>
    </CFooter>
  )
}

export default React.memo(AppFooter)
