export interface QueryParameters {
  [key: string]: string
}

type Method = "GET" | "POST" | "PUT" | "DELETE"
export interface RequestParameters {
  method: Method
  url: string
  entity?: any
  auth?: string
}

export function request<T>(parameters: RequestParameters): Promise<T> {
  return executeRequest(parameters) as Promise<T>;
}

function executeRequest(parameters: RequestParameters) {
  const {method, url, entity, auth} = parameters
  return new Promise((resolve, reject) => {
    const xhr = new XMLHttpRequest()
    xhr.open(method, url, true)
    xhr.setRequestHeader("Content-Type", "application/json");
    if (auth) {
      xhr.setRequestHeader("Authorization", "Bearer " + auth);
    }
    xhr.onreadystatechange = () => {
      if (xhr.readyState == 4) {
        if (xhr.status == 200) {
          let value
          try {
            value = JSON.parse(xhr.responseText)
          } catch (e) {
            value = xhr.response
          }
          resolve(value)
        } else if (xhr.status == 0) {
          reject({
            result: {
              success: false,
              value: "connection error"
            },
            xhr: xhr
          })
        } else {
          if (xhr.status == 401 || xhr.status == 403) {
            //session.logout()
          }
          try {
            const jsonErrors = JSON.parse(xhr.responseText)
            if (jsonErrors.errorMessages) {
              reject(jsonErrors.errorMessages.map((_: any) => (_.defaultMessage != null && _.defaultMessage != "") ? _.defaultMessage : _.key))
            } else {
              reject({
                result: jsonErrors,
                xhr: xhr
              })
            }
          } catch (e) {
            reject({
              result: xhr.response,
              xhr: xhr
            })
          }
        }
      }
    }
    if (entity) {
      xhr.send(JSON.stringify(entity))
    } else {
      xhr.send()
    }
  })
}

export function buildUrl(url: string, parameters: QueryParameters): string {
  const parts = []
  for (let key in parameters) {
    if (parameters.hasOwnProperty(key) && parameters[key] !== null) {
      parts.push(`${key}=${parameters[key]}`)
    }
  }
  if (parts.length > 0) {
    return `${url}?${parts.join("&")}`
  } else {
    return url
  }
}