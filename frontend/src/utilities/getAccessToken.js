const getAccessToken = () => {
  const storedData = localStorage.getItem('user');
  const parsedData = JSON.parse(storedData);
  return parsedData?.state?.user?.accessToken;
};

export default getAccessToken;
