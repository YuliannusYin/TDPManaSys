import request from './request'

export function getPortraitRadar(userId) {
  return request({ url: `/portrait/${userId}/radar`, method: 'get' })
}

export function getPortraitDashboard(userId) {
  return request({ url: `/portrait/${userId}/dashboard`, method: 'get' })
}

export function getPortraitTrend(userId) {
  return request({ url: `/portrait/${userId}/trend`, method: 'get' })
}

export function getPortraitCompare(userIds) {
  return request({ url: `/portrait/compare`, method: 'get', params: { userIds } })
}

export function getPortraitTeachers() {
  return request({ url: `/portrait/teachers`, method: 'get' })
}

export function getPortraitDistribution(userId) {
  return request({ url: `/portrait/${userId}/distribution`, method: 'get' })
}

export function getAggregatedDashboard() {
  return request({ url: `/portrait/dashboard`, method: 'get' })
}