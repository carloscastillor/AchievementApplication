import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/resource">
        <Translate contentKey="global.menu.entities.resource" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/walkthrough">
        <Translate contentKey="global.menu.entities.walkthrough" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/message">
        <Translate contentKey="global.menu.entities.message" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/videogame">
        <Translate contentKey="global.menu.entities.videogame" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/achievement">
        <Translate contentKey="global.menu.entities.achievement" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/personalized-achievement">
        <Translate contentKey="global.menu.entities.personalizedAchievement" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/community">
        <Translate contentKey="global.menu.entities.community" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/post">
        <Translate contentKey="global.menu.entities.post" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/like-post">
        <Translate contentKey="global.menu.entities.likePost" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/like-message">
        <Translate contentKey="global.menu.entities.likeMessage" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu as React.ComponentType<any>;
