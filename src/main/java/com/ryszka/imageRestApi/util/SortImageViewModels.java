package com.ryszka.imageRestApi.util;

import com.ryszka.imageRestApi.viewModels.request.GetImagesByTagRequest;
import com.ryszka.imageRestApi.viewModels.response.UserImageViewModel;

import javax.swing.text.html.ImageView;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SortImageViewModels {
    public enum SortType {
        SORT_BY_DOWNLOADS {
            @Override
            public List<UserImageViewModel> sort(List<UserImageViewModel> source) {
                return source.stream()
                        .sorted(Comparator.comparingInt(UserImageViewModel::getDownloaded).reversed())
                        .collect(Collectors.toList());
            }
        }, SORT_BY_LIKES {
            @Override
            public List<UserImageViewModel> sort(List<UserImageViewModel> source) {
                return source.stream()
                        .sorted(Comparator.comparingInt(UserImageViewModel::getLiked).reversed())
                        .collect(Collectors.toList());
            }
        }, SORT_BY_LIKES_AND_DOWNLOADS {
            @Override
            public List<UserImageViewModel> sort(List<UserImageViewModel> source) {
                return source.stream()
                        .sorted(Comparator.comparingInt(UserImageViewModel::getLiked).reversed()
                                .thenComparingInt(UserImageViewModel::getDownloaded).reversed())
                        .collect(Collectors.toList());
            }
        };

        public abstract List<UserImageViewModel> sort(List<UserImageViewModel> source);
    }

    public static List<UserImageViewModel> sortRequest(GetImagesByTagRequest request,
                                                       List<UserImageViewModel> source) {
        return request.isMostDownloaded() ? SortType.SORT_BY_DOWNLOADS.sort(source) :
                request.isMostLiked() ? SortType.SORT_BY_LIKES.sort(source) :
                        request.isMostDownloaded() && request.isMostLiked() ?
                                SortType.SORT_BY_LIKES_AND_DOWNLOADS.sort(source) :
                                source;
    }
}
