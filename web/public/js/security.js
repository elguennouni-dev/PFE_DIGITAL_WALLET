document.addEventListener('DOMContentLoaded', () => {
    const securityModal = document.getElementById('securityModal')
    const activityList = document.getElementById('activity-list-container')
    const loadingState = document.getElementById('history-loading')
    const errorState = document.getElementById('history-error')
    const errorMessage = document.getElementById('history-error-message')
    const emptyState = document.getElementById('history-empty')
  
    function formatTimeAgo(isoString) {
      if (!isoString) return 'Unknown time'
      const now = new Date()
      const past = new Date(isoString)
      const diff = Math.floor((now - past) / 1000)
      if (diff < 60) return `${diff} seconds ago`
      if (diff < 3600) return `${Math.floor(diff / 60)} minutes ago`
      if (diff < 86400) return `${Math.floor(diff / 3600)} hours ago`
      if (diff < 172800 && now.getDate() > past.getDate()) return `Yesterday at ${past.toLocaleTimeString('en-US', { hour: 'numeric', minute: '2-digit', hour12: true })}`
      return past.toLocaleDateString('en-US', { month: 'long', day: 'numeric', year: 'numeric' })
    }
  
    function getDeviceDetails(deviceString) {
      const d = deviceString ? deviceString.toLowerCase() : ''
      if (d.includes('mobile') || d.includes('iphone') || d.includes('android')) return { iconName: 'smartphone', iconClass: 'mobile', displayName: deviceString }
      if (d.includes('tablet') || d.includes('ipad')) return { iconName: 'tablet', iconClass: 'tablet', displayName: deviceString }
      return { iconName: 'monitor', iconClass: 'desktop', displayName: deviceString || 'Unknown Device' }
    }
  
    function getStatusInfo(reason) {
      if (reason === 'Login Success') return { text: 'Success', cssClass: 'status-success' }
      if (reason && reason.toLowerCase().includes('fail')) return { text: 'Failed', cssClass: 'status-danger' }
      return { text: reason || 'Unknown', cssClass: 'status-warning' }
    }
  
    async function fetchAndDisplayLoginHistory() {
      loadingState.style.display = 'block'
      errorState.style.display = 'none'
      emptyState.style.display = 'none'
      activityList.innerHTML = ''
      try {
        const userData = JSON.parse(localStorage.getItem("userData"))
        if (!userData || !userData.id || !userData.token) throw new Error("User data not found.")
        const response = await fetch(`http://84.247.142.209:8080/login-history/${userData.id}`, {
          method: 'GET',
          headers: { 'Authorization': `Bearer ${userData.token}` }
        })
        if (!response.ok) throw new Error(`API request failed with status ${response.status}`)
        const historyData = await response.json()
        loadingState.style.display = 'none'
        if (Array.isArray(historyData) && historyData.length > 0) {
          activityList.innerHTML = historyData.map(item => {
            const deviceDetails = getDeviceDetails(item.device)
            const statusInfo = getStatusInfo(item.reason)
            return `<li class="activity-item">
              <div class="device-icon ${deviceDetails.iconClass}"><i data-feather="${deviceDetails.iconName}"></i></div>
              <div class="activity-details">
                <span class="device-info">${deviceDetails.displayName}</span>
                <span class="location-info">${item.location || 'Unknown Location'}</span>
              </div>
              <div class="activity-status">
                <span class="time-info">${formatTimeAgo(item.loginTime)}</span>
                <span class="status-badge ${statusInfo.cssClass}">${statusInfo.text}</span>
              </div>
            </li>`
          }).join('')
        } else {
          emptyState.style.display = 'block'
        }
      } catch (err) {
        loadingState.style.display = 'none'
        errorMessage.textContent = `Error: ${err.message}`
        errorState.style.display = 'block'
      } finally {
        if (window.feather) window.feather.replace()
      }
    }
  
    const openModalButton = document.querySelector('a[href="#security"]')
    if (openModalButton) {
      openModalButton.addEventListener('click', (e) => {
        e.preventDefault()
        securityModal.style.display = 'block'
        fetchAndDisplayLoginHistory()
      })
    }
  
    const closeBtn = securityModal.querySelector('.close-btn')
    if (closeBtn) {
      closeBtn.addEventListener('click', () => {
        securityModal.style.display = 'none'
      })
    }
  })
  