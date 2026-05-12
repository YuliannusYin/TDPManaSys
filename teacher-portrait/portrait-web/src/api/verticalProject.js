import request from './request'

export function pageVerticalProjects(params) {
  return request({
    url: '/vertical-projects',
    method: 'get',
    params
  })
}

export function getVerticalProject(id) {
  return request({
    url: `/vertical-projects/${id}`,
    method: 'get'
  })
}

export function createVerticalProject(data) {
  return request({
    url: '/vertical-projects',
    method: 'post',
    data
  })
}

export function updateVerticalProject(id, data) {
  return request({
    url: `/vertical-projects/${id}`,
    method: 'put',
    data
  })
}

export function deleteVerticalProject(id) {
  return request({
    url: `/vertical-projects/${id}`,
    method: 'delete'
  })
}