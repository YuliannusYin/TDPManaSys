import request from './request'

export function pageCompetitions(params) {
  return request({ url: '/competitions', method: 'get', params })
}

export function getCompetition(id) {
  return request({ url: `/competitions/${id}`, method: 'get' })
}

export function createCompetition(data) {
  return request({ url: '/competitions', method: 'post', data })
}

export function updateCompetition(id, data) {
  return request({ url: `/competitions/${id}`, method: 'put', data })
}

export function deleteCompetition(id) {
  return request({ url: `/competitions/${id}`, method: 'delete' })
}
