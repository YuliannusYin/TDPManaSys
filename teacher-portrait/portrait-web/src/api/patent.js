import request from './request'

export function pagePatents(params) {
  return request({ url: '/patents', method: 'get', params })
}

export function getPatent(id) {
  return request({ url: `/patents/${id}`, method: 'get' })
}

export function createPatent(data) {
  return request({ url: '/patents', method: 'post', data })
}

export function updatePatent(id, data) {
  return request({ url: `/patents/${id}`, method: 'put', data })
}

export function deletePatent(id) {
  return request({ url: `/patents/${id}`, method: 'delete' })
}

export function transferPatent(id, data) {
  return request({ url: `/patents/${id}/transfer`, method: 'post', data })
}

export function getPatentTransfers(id) {
  return request({ url: `/patents/${id}/transfers`, method: 'get' })
}
