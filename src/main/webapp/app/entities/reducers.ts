import resource from 'app/entities/resource/resource.reducer';
import walkthrough from 'app/entities/walkthrough/walkthrough.reducer';
import message from 'app/entities/message/message.reducer';
import videogame from 'app/entities/videogame/videogame.reducer';
import achievement from 'app/entities/achievement/achievement.reducer';
import personalizedAchievement from 'app/entities/personalized-achievement/personalized-achievement.reducer';
import community from 'app/entities/community/community.reducer';
import post from 'app/entities/post/post.reducer';
import likePost from 'app/entities/like-post/like-post.reducer';
import likeMessage from 'app/entities/like-message/like-message.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  resource,
  walkthrough,
  message,
  videogame,
  achievement,
  personalizedAchievement,
  community,
  post,
  likePost,
  likeMessage,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
