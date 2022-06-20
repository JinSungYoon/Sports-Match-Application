package core.player;

import core.player.entity.BaseEntity;

public class MockEntity extends BaseEntity {
	public static <T extends BaseEntity> T mock(Class<T> clazz, long id) {
        try {
            T mock = clazz.newInstance();
            mock.id = id;
            return mock;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
