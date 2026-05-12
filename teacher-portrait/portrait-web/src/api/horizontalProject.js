import request from './request'

export function pageHorizontalProjects(params) {
  return request({
    url: '/horizontal-projects',
    method: 'get',
    params
  })
}

export function getHorizontalProject(id) {
  return request({
    url: `/horizontal-projects/${id}`,
    method: 'get'
  })
}

export function createHorizontalProject(data) {
  return request({
    url: '/horizontal-projects',
    method: 'post',
    data
  })
}

export function updateHorizontalProject(id, data) {
  return request({
    url: `/horizontal-projects/${id}`,
    method: 'put',
    data
  })
}

export function deleteHorizontalProject(id) {
  return request({
    url: `/horizontal-projects/${id}`,
    method: 'delete'
  })
}
