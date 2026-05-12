import request from './request'

export function pageSoftwareCopyrights(params) {
  return request({ url: '/software-copyrights', method: 'get', params })
}

export function getSoftwareCopyright(id) {
  return request({ url: `/software-copyrights/${id}`, method: 'get' })
}

export function createSoftwareCopyright(data) {
  return request({ url: '/software-copyrights', method: 'post', data })
}

export function updateSoftwareCopyright(id, data) {
  return request({ url: `/software-copyrights/${id}`, method: 'put', data })
}

export function deleteSoftwareCopyright(id) {
  return request({ url: `/software-copyrights/${id}`, method: 'delete' })
}
