import { CopySlash, CodeXml, Infinity, Bird } from 'lucide-react';

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
