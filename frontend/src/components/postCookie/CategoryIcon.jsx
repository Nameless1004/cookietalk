import { CopySlash, CodeXml, Infinity, Bird } from 'lucide-react';

// TODO: 나머지 상태들 추가
const CategoryIcon = ({ category }) => {
  switch (category) {
    case 'backend':
      return <CopySlash />;
    case 'frontend':
      return <CodeXml />;
    case 'devops':
      return <Infinity />;
    default:
      return <Bird />;
  }
};

export default CategoryIcon;
