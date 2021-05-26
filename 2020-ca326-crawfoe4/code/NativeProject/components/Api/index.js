const axios = require('axios').default;
console.log('api');
// axios.defaults.baseURL = 'http://pygmalion.redbrick.dcu.ie:7726';

var instance = axios.create({
  baseURL: 'http://pygmalion.redbrick.dcu.ie:7726',
  timeout: 1000,
});

const Api = {
  getQueue: async () => {
    try {
      console.log('getting');
      const resp = await instance.get('/queue/');
      console.log('Complete response:', resp);
      if (resp.status === 200) {
        console.log('resp.data:', resp.data);
        return resp.data;
      }
      return {isError: true};
    } catch (e) {
      return {isError: true};
    }
  },
  postTest: async () => {
    const headers = {
      '1': 'open',
    };
    console.log(headers);
    try {
      console.log('post');
      const resp = await instance.post('/addqueue/', headers);
      console.log('Complete response:', resp);
      if (resp.status === 200) {
        console.log('resp.data:', resp.data);
        return resp.data;
      }
      return {isError: true};
    } catch (e) {
      return {isError: true};
    }
  },
  get: async (endpoint, secureToken, params) => {
    try {
      const resp = await axios.get(
        `/safehouse/api/${endpoint}/${secureToken}`,
        {
          params: {
            data: params.data,
          },
        },
      );
      if (resp.status === 200) {
        return resp.data;
      }
      return {isError: true};
    } catch (e) {
      return {isError: true};
    }
  },
  postPublicKey: async (publicKey, email) => {
    const headers = {
      Key: publicKey,
      Email: email,
    };
    try {
      const resp = await instance.post('/create-account/', headers);
      if (resp.status === 200) {
        return resp.data;
      }
      return 'Error: ' + resp;
    } catch (e) {
      return 'Error' + e;
    }
  },
  postVerifySignature: async signature => {
    console.log('postVerifySignature');
    const headers = {
      Signature: signature,
    };
    console.log(headers);
    try {
      const resp = await instance.post('/addkey/', headers);
      if (resp.status === 200) {
        return resp.data;
      }
      return {isError: true};
    } catch (e) {
      return {isError: true};
    }
  },
  postRegistorNewLockId: async (signature, payload) => {
    return true;
    //   try {
    //     const resp = await Axios.post('/safehouse/api/registorNewLock/', {
    //       signature: signature,
    //       payload: payload,
    //     });
    //     if (resp.status === 200) {
    //       return resp.data;
    //     }
    //     return {isError: true};
    //   } catch (e) {
    //     return {isError: true};
    //   }
    // },
  },
  postTestLock: async (signature, payload) => {
    const headers = {
      Signature: signature,
      Payload: payload,
    };
    console.log('header:', headers);
    try {
      console.log('post');
      const resp = await instance.post('/test-lock/', headers);
      console.log('Complete response:', resp);
      if (resp.status === 200) {
        console.log('resp.data:', resp.data);
        return resp.data;
      }
      return {isError: true};
    } catch (e) {
      return {isError: true};
    }
  },
  postTestAuth: async (signature, payload) => {
    const headers = {
      Signature: signature,
      Payload: payload,
    };
    console.log(headers);
    try {
      console.log('post');
      const resp = await instance.post('/postTestAuth/', headers);
      console.log('Complete response:', resp);
      if (resp.status === 200) {
        console.log('resp.data:', resp.data);
        return resp.data;
      }
      return {isError: true};
    } catch (e) {
      return {isError: true};
    }
  },
  postInitLock: async (signature, payload) => {
    const headers = {
      Signature: signature,
      Payload: payload,
    };
    console.log(headers);
    try {
      console.log('post');
      const resp = await instance.post('/init-lock/', headers);
      console.log('Complete response:', resp);
      if (resp.status === 200) {
        console.log('resp.data:', resp.data);
        return resp.data;
      }
      return {isError: true};
    } catch (e) {
      return {isError: true};
    }
  },
  postOpenLock: async (signature, payload) => {
    const headers = {
      Signature: signature,
      Payload: payload,
    };
    console.log(headers);
    try {
      console.log('post');
      const resp = await instance.post('/open-lock/', headers);
      console.log('Complete response:', resp);
      if (resp.status === 200) {
        console.log('resp.data:', resp.data);
        return resp.data;
      }
      return {isError: true};
    } catch (e) {
      return {isError: true};
    }
  },
  postAdminAddUser: async (signature, payload) => {
    const headers = {
      Signature: signature,
      Payload: payload,
    };
    console.log(headers);
    try {
      console.log('post');
      const resp = await instance.post('/admin-add-user/', headers);
      console.log('Complete response:', resp);
      if (resp.status === 200) {
        console.log('resp.data:', resp.data);
        return resp.data;
      }
      return {isError: true};
    } catch (e) {
      return {isError: true};
    }
  },
};

export default Api;
