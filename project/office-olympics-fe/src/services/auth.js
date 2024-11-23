import api from './api';

// 사용자 로그인을 위한 함수
export const login = async (credentials) => {
  const response = await api.post('/auth/login', credentials);
  console.log('Raw API Response:', response.data);

  return {
    user: {
      id: response.data.loginUserId,
      nickname: response.data.nickname
    },
    olympicsId: response.data.olympicsId || null,
    token: null,
  };
};

// 회원가입의 경우, 전역 설정과 다른 Content-Type을 지정해야 하므로 헤더만 따로 설정합니다
export const register = async (data) => {
  return api.post('/auth/register', data, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });
};

// 로그아웃 함수
export const logout = async () => {
  return api.post('/auth/logout');
};
