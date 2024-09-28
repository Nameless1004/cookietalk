const FileInput = ({ label, name, value, setValue, acceptType, ...props }) => {
  const handleFileChange = (e) => {
    setValue({ ...value, [e.target.name]: e.target.files[0] });
  };

  return (
    <div className='flex gap-5'>
      {label ? <span className='font-bold'>{label}</span> : null}
      <input
        type='file'
        name={name}
        accept={acceptType}
        onChange={handleFileChange}
        {...props}
      />
    </div>
  );
};

export default FileInput;
