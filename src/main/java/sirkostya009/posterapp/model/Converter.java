package sirkostya009.posterapp.model;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import sirkostya009.posterapp.model.dao.AppUser;
import sirkostya009.posterapp.model.dao.Poster;
import sirkostya009.posterapp.model.common.PosterModel;
import sirkostya009.posterapp.model.common.AppUserModel;

import java.util.List;

public class Converter {

    @Contract("_ -> new")
    public static @NotNull PosterModel posterModel(Poster poster) {
        return posterModel(poster, false);
    }

    @Contract("_ -> new")
    public static @NotNull AppUserModel userInfo(AppUser user) {
        return userInfo(user, false);
    }

    @Contract("_, _ -> new")
    public static @NotNull AppUserModel userInfo(@NotNull AppUser user, boolean showEmail) {
        return new AppUserModel(
                user.getId(),
                user.getUsername(),
                user.getProfilePictureFilename(),
                showEmail ? user.getEmail() : null,
                user.getBio()
        );
    }

    @Contract("_, _ -> new")
    public static @NotNull PosterModel posterModel(@NotNull Poster poster, boolean includeUserInfo) {
        return new PosterModel(
                poster.getText(),
                includeUserInfo ? userInfo(poster.getAuthor()) : null,
                poster.getLikes().contains(poster.getAuthor()),
                poster.getLikes().size(),
                poster.getId(),
                poster.getPostedAt(),
                poster.getLastEditedAt()
        );
    }

    @Contract("_, _ -> new")
    public static @NotNull Page<PosterModel> pageOfPostersToPosterModels(@NotNull Page<Poster> page, boolean includeUserInfo) {
        return new PageImpl<>(listOfPostersToPosterModels(page.getContent(), includeUserInfo), page.getPageable(), page.getTotalElements());
    }

    public static List<PosterModel> listOfPostersToPosterModels(@NotNull List<Poster> posters, boolean includeUserInfo) {
        return posters.stream().map(poster -> Converter.posterModel(poster, includeUserInfo)).toList();
    }

}
