package im.delight.android.ddp;

/*
 * Copyright (c) delight.im <info@delight.im>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.os.Handler;
import android.os.Looper;
import java.util.LinkedList;
import java.util.List;

/** Wrapper that executes all registered callbacks on the correct thread behind the scenes */
public class CallbackProxy implements MeteorCallback {

	private final List<MeteorCallback> mCallbacks = new LinkedList<MeteorCallback>();
	private final Handler mUiHandler = new Handler(Looper.getMainLooper());

	public CallbackProxy() { }

	public void addCallback(final MeteorCallback callback) {
		mCallbacks.add(callback);
	}

	public void removeCallback(final MeteorCallback callback) {
		mCallbacks.remove(callback);
	}

	public void removeCallbacks() {
		mCallbacks.clear();
	}

	@Override
	public void onConnect(final boolean signedInAutomatically) {
		// iterate over all the registered callbacks
		for (final MeteorCallback callback : mCallbacks) {
			// if the callback exists
			if (callback != null) {
				// execute the callback on the main thread
				mUiHandler.post(new Runnable() {

					@Override
					public void run() {
						// run the proxied method with the same parameters
						callback.onConnect(signedInAutomatically);
					}

				});
			}
		}
	}

	@Override
	public void onDisconnect() {
		// iterate over all the registered callbacks
		for (final MeteorCallback callback : mCallbacks) {
			// if the callback exists
			if (callback != null) {
				// execute the callback on the main thread
				mUiHandler.post(new Runnable() {

					@Override
					public void run() {
						// run the proxied method with the same parameters
						callback.onDisconnect();
					}

				});
			}
		}
	}

	@Override
	public void onDataAdded(final String collectionName, final String documentID, final String newValuesJson) {
		// iterate over all the registered callbacks
		for (final MeteorCallback callback : mCallbacks) {
			// if the callback exists
			if (callback != null) {
				// execute the callback on the main thread
				mUiHandler.post(new Runnable() {

					@Override
					public void run() {
						// run the proxied method with the same parameters
						callback.onDataAdded(collectionName, documentID, newValuesJson);
					}

				});
			}
		}
	}

	@Override
	public void onDataChanged(final String collectionName, final String documentID, final String updatedValuesJson, final String removedValuesJson) {
		// iterate over all the registered callbacks
		for (final MeteorCallback callback : mCallbacks) {
			// if the callback exists
			if (callback != null) {
				// execute the callback on the main thread
				mUiHandler.post(new Runnable() {

					@Override
					public void run() {
						// run the proxied method with the same parameters
						callback.onDataChanged(collectionName, documentID, updatedValuesJson, removedValuesJson);
					}

				});
			}
		}
	}

	@Override
	public void onDataRemoved(final String collectionName, final String documentID) {
		// iterate over all the registered callbacks
		for (final MeteorCallback callback : mCallbacks) {
			// if the callback exists
			if (callback != null) {
				// execute the callback on the main thread
				mUiHandler.post(new Runnable() {

					@Override
					public void run() {
						// run the proxied method with the same parameters
						callback.onDataRemoved(collectionName, documentID);
					}

				});
			}
		}
	}

	@Override
	public void onException(final Exception e) {
		// iterate over all the registered callbacks
		for (final MeteorCallback callback : mCallbacks) {
			// if the callback exists
			if (callback != null) {
				// execute the callback on the main thread
				mUiHandler.post(new Runnable() {

					@Override
					public void run() {
						// run the proxied method with the same parameters
						callback.onException(e);
					}

				});
			}
		}
	}

	public ResultListener forResultListener(final ResultListener callback) {
		return new ResultListener() {

			@Override
			public void onSuccess(final String result) {
				// if the callback exists
				if (callback != null) {
					// execute the callback on the main thread
					mUiHandler.post(new Runnable() {

						@Override
						public void run() {
							// run the proxied method with the same parameters
							callback.onSuccess(result);
						}

					});
				}
			}

			@Override
			public void onError(final String error, final String reason, final String details) {
				// if the callback exists
				if (callback != null) {
					// execute the callback on the main thread
					mUiHandler.post(new Runnable() {

						@Override
						public void run() {
							// run the proxied method with the same parameters
							callback.onError(error, reason, details);
						}

					});
				}
			}

		};
	}

	public SubscribeListener forSubscribeListener(final SubscribeListener callback) {
		return new SubscribeListener() {

			@Override
			public void onSuccess() {
				// if the callback exists
				if (callback != null) {
					// execute the callback on the main thread
					mUiHandler.post(new Runnable() {

						@Override
						public void run() {
							// run the proxied method with the same parameters
							callback.onSuccess();
						}

					});
				}
			}

			@Override
			public void onError(final String error, final String reason, final String details) {
				// if the callback exists
				if (callback != null) {
					// execute the callback on the main thread
					mUiHandler.post(new Runnable() {

						@Override
						public void run() {
							// run the proxied method with the same parameters
							callback.onError(error, reason, details);
						}

					});
				}
			}

		};
	}

	public UnsubscribeListener forUnsubscribeListener(final UnsubscribeListener callback) {
		return new UnsubscribeListener() {

			@Override
			public void onSuccess() {
				// if the callback exists
				if (callback != null) {
					// execute the callback on the main thread
					mUiHandler.post(new Runnable() {

						@Override
						public void run() {
							// run the proxied method with the same parameters
							callback.onSuccess();
						}

					});
				}
			}

		};
	}

}
//{"_id":"avAGYHjtqYDZW9nsS","createdAt":{"$date":1467880301687},"services":{"google":{"accessToken":"ya29.Ci88A9QdPTXi3_cS_SQvRwJw_mDXNPN2DgZFXvJ7Okv8zEY1mTpUodGLTiFyIQv1yg","idToken":"eyJhbGciOiJSUzI1NiIsImtpZCI6IjEwNDYyNTQ2NWY2ZDRjN2QyMTRlMzMyNjkxM2M1YTVlNDUwNTY5OWMifQ.eyJpc3MiOiJhY2NvdW50cy5nb29nbGUuY29tIiwiYXRfaGFzaCI6IlQyZVVQZTU5Nkc3enhJRERwYXpnOFEiLCJhdWQiOiIyMjAyNjEwNzY3NS1qZmVvaWpocjVxbHB2ZHMwdjBkdGx2cHN2OG5qc2lvcS5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsInN1YiI6IjEwMzA2MTM2NDkwMzI3NDY0MjEwNiIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJhenAiOiIyMjAyNjEwNzY3NS1qZmVvaWpocjVxbHB2ZHMwdjBkdGx2cHN2OG5qc2lvcS5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsImVtYWlsIjoiYWh1amEua3VzaC53b3JrQGdtYWlsLmNvbSIsImlhdCI6MTQ3MDkxNTQ4NCwiZXhwIjoxNDcwOTE5MDg0fQ.nohcr2yJsyioMyg4FZ3imr3Itl7z0gIkHGBWNEFnCD_l3rtpPKlY5YOMYLWczINPlSHAg1_EtHLadz3R4ZiqHfoLxgNf4xT5towWYYFjgDBhenqxfjmaX1m9Vu0nvOjfTvPyFdWKq_RH8Wo69ropKcs_6BVr_XK1Dg1E4IgrcgbWYJv6Uu1nsaGFf1Ok_B4mumM8fr6XNLTq0mrljnvx9atp55PzZCcHqXsKIFt5uNvko4ZYjgJC9WmxT5_bZm0mpnmUUWpITtFV_lK3ZeP8owelMRa0nzgijW-ekCSwMsjVs1Ki2dpL5SfAEzgTxy-YwPp66r3CcEcTwZKYSY0BGQ","expiresAt":1470919164961,"scope":["https://www.googleapis.com/auth/userinfo.email","https://www.googleapis.com/auth/calendar","https://www.googleapis.com/auth/plus.me"],"id":"103061364903274642106","email":"ahuja.kush.work@gmail.com","verified_email":true,"name":"Kush Ahuja","given_name":"Kush","family_name":"Ahuja","picture":"https://lh3.googleusercontent.com/-N81f9GvOb88/AAAAAAAAAAI/AAAAAAAAAGY/GS9WVcM-fPQ/photo.jpg","gender":"male","refreshToken":"1/hQrkr1jQ9PJXPIDI6tZMy1f6vjhoryNjQWJTiH2iWE4","locale":"en"},"resume":{"loginTokens":[{"when":{"$date":1467880301725},"hashedToken":"wMTN7++80Fn/nLtG/SN3NBoxXfYc3iWKAneQL1akLmc="},{"when":{"$date":1468034501219},"hashedToken":"ffS7P59Tn7+qZSeu2J8LdvPsuwR+62gsk++OKSeCo24="},{"when":{"$date":1468527131400},"hashedToken":"GbzA7cafXlD2AiCMzHbGBLqbO5exZFnmHXPK0XisPUc="},{"when":{"$date":1468695283150},"hashedToken":"wR6gs5BFC3quBhSfw+I5ds2FFQNOG/dSFluRbEShWyU="},{"when":{"$date":1468733451985},"hashedToken":"lYIvTWKrLAgR0XNTPXmPcwVVK0cCNtQHqaAj+xvoe7I="},{"when":{"$date":1468993342753},"hashedToken":"VXdS1QrD1E0VsJFg4VHTzTwsOBnedW31qQLTvIjZTI0="},{"when":{"$date":1469083230465},"hashedToken":"3fx05PO8VYa3IVmp/06/rDmZise4LM+8WhefhFfmKQo="},{"when":{"$date":1469083509904},"hashedToken":"CNYSo9NrfgTQj7Ug+1gf1Phs0GgrZLl9Bl2YnzydWwk="},{"when":{"$date":1469155188596},"hashedToken":"6cK8yVKFXJt9Vb3nbycihcDS/7IaIJ9cAuT+lJDzvKY="},{"when":{"$date":1469312178229},"hashedToken":"5cX0UU9lis76sx41JQFfmN4yeFI95SsJ00uDfBOYkdI="},{"when":{"$date":1469325549897},"hashedToken":"EJs2Y7lrjixV1JatNlS0AVeUwHAG8nQiXokQIvuqFbQ="},{"when":{"$date":1469499242732},"hashedToken":"4G2MLcQxM910kjI8UOV6ljc+j1OviTytvFxdH9lNIlo="},{"when":{"$date":1469505053354},"hashedToken":"VWG93Yi0Uq3eXmeB2Dlz2/+4mItlUyVSsv5wCoJ6E84="},{"when":{"$date":1469518019190},"hashedToken":"7gnspR+NmQCbg8Gm0k+7utXpe2zLK+1YQYqPDiziUOE="},{"when":{"$date":1469520732556},"hashedToken":"QYSYhLGjYIxPFz+dhRDK6EIyns4yj1FkF+ekxn5rUmQ="},{"when":{"$date":1469588139866},"hashedToken":"2DkbD2kLkZW5PV3KeqSEYfuu6wqdafi8ZHfG4ZjN6yA="},{"when":{"$date":1469588672414},"hashedToken":"AGy4aChRH2gfXM7deBvWGxfQovRa/AFL8UdXzkdHHOI="},{"when":{"$date":1469588738800},"hashedToken":"U2diobYo7l9LaDICGDI2WTd1fEkF133qcJRoVfqXVQ8="},{"when":{"$date":1469588812169},"hashedToken":"u9yZ8Z3JFXz34Za83RPYvlTfb+Q1ImOS5AbFbHEg3xw="},{"when":{"$date":1469589295213},"hashedToken":"Ez1YQvrAYkuqFfkhApZAgF8DIz2krfUP5BiuYQXwydA="},{"when":{"$date":1469595580274},"hashedToken":"kUrSp3h05/qxmdL7eZFKgUsAWl26kXswsgnUcINextc="},{"when":{"$date":1469595658460},"hashedToken":"3yGfxvlxtuBz3QENPMZtttQoSfZOXV2koAIHgKpANZQ="},{"when":{"$date":1469596143037},"hashedToken":"p8SIlPRG+zXWvOtGrYcRo6+BBBAvnzkD5GxSMR0ND6Q="},{"when":{"$date":1469596210197},"hashedToken":"ofAcKE4pbiBDPBNlwskzKkZB8xUR3v6G3/hb8UxJSAw="},{"when":{"$date":1469596447417},"hashedToken":"IjhnGjrwZjwd9hFqPdNChcKi/p6/DYb37DaGskNUM7E="},{"when":{"$date":1469606925204},"hashedToken":"bxkvqC5JR3gTZZAT7xpDycIhWM/hUaME3w4lMQHOGGk="},{"when":{"$date":1469607071307},"hashedToken":"yu4+ZXefzjSEu1DIRZNlGpecnl6fBsGJjRKBLWF5bYY="},{"when":{"$date":1469607392935},"hashedToken":"ujWLD9pe8Ch5M1b5JjtSD+RWgHX6Whm2jFTQBJD1/dg="},{"when":{"$date":1469608248234},"hashedToken":"wQqz/UoLVCbTjcTkE6SEgpmP5Oqf5c+0A9AFYMEDsZM="},{"when":{"$date":1469655253876},"hashedToken":"HLzbQf/5yWBw7Pa+rY7uBlgsTjToaluURMH859GB1Xg="},{"when":{"$date":1469747982792},"hashedToken":"O+1XnkPop+Biaubw1ronuEFBZ3acTUE5UMLlhpn4e6k="},{"when":{"$date":1469748374793},"hashedToken":"tEQeinqZtfwA2S5Gbz1OpvFUGvuoHH0V88kAZTqbDH0="},{"when":{"$date":1469771177741},"hashedToken":"tYkkP42slAl6sHfWfE2+tai6fxYVJOKHIA/rtJDhVe4="},{"when":{"$date":1469771762379},"hashedToken":"yYLCiN/fm/e+foBn3QcDLtmrGk03S2rHndquOL4YHOU="},{"when":{"$date":1469781042102},"hashedToken":"LhJgdVTZ0uPI+tA3bQEFFZ/yJFfC6Rsjz9rxrm66hcU="},{"when":{"$date":1469783750239},"hashedToken":"mSmSLIbBgBf6FbywARlt9dCYyYZBPjgnZE3bWVN9dGQ="},{"when":{"$date":1469783973955},"hashedToken":"zpy15QBklAIqkhV7lM8hNuczVVGm63trOfG5n9A4WQs="},{"when":{"$date":1469784170515},"hashedToken":"MRdNkTH9hcU0N3KD2f4I6A8WWDEZCugHs3SUHXd4qlY="},{"when":{"$date":1469784633058},"hashedToken":"rPlxFwTKr8blNjoT0Le+ITYBRDS/zJVAjlirQD9lvDw="},{"when":{"$date":1469829324848},"hashedToken":"CLejEAH914nLUs0mRMJXW+hNXn1jHwI2rFXDMlvQaj8="},{"when":{"$date":1469838614427},"hashedToken":"kEYaCjxKQ1TJ4/MCJ7D7nKNtYW0ydUZ4SoweM+p/rXo="},{"when":{"$date":1470124067982},"hashedToken":"pGIVxiOLpVw7AN6Jud0nolv9E3VzmzizgxY5dVQno+M="},{"when":{"$date":1470124623067},"hashedToken":"yqb1JCBh/zF/sBOXxJSzg9CSZr7/2Vf0hQs7hqHS+wk="},{"when":{"$date":1470205517794},"hashedToken":"zBAyMn7TXN3lu/LD+/YCUcYB8qCyS/DUizkO5k++H64="},{"when":{"$date":1470261641269},"hashedToken":"ZgXmdERSXRE5oJv7Vb9Qy9VwdpYbfrJM+ZbEUAOGu50="},{"when":{"$date":1470266744884},"hashedToken":"gv211PnsKLgTOEZA++rQabaHgFu9FN+hLyunggenMrs="},{"when":{"$date":1470293871038},"hashedToken":"kpGrFb2RLlIRLZOyAZZnqzxIxXEwpSFU3KIlgCYOH00="},{"when":{"$date":1470294699678},"hashedToken":"v8/10Jd7179/dVFcmaPNK1Ye29PzseplY9l5exG1Tac="},{"when":{"$date":1470296282946},"hashedToken":"tERdauwKrG0Ka67M9ppW22/NLCUneeUv05RgGQnMurQ="},{"when":{"$date":1470377629335},"hashedToken":"HX2BcK1G8mScQUs3h2Mix6UUfo2MDACUzCe8/v9sf4M="},{"when":{"$date":1470380684799},"hashedToken":"WXQapuX7rIJV8Iup2BEpwxMQFngRrGOumnilJn2UnrE="},{"when":{"$date":1470606808918},"hashedToken":"W0qq0kxBOMMBiVuaiDn49kWwxqpQsYeaiDlMMtRLYU8="},{"when":{"$date":1470640290412},"hashedToken":"p12GyJ28JIVSmVV27MqZghTZqpYxeiYy+FEVfqd3L4w="},{"when":{"$date":1470680475633},"hashedToken":"2QOoAJq/zOx9jnkktsPyoSTCfovRPG3XdiyhkxVtd6Y="},{"when":{"$date":1470682088905},"hashedToken":"ACSj1lt/3PPdvrzbLzv2RWgvtlJsaia9fABZSm4SB24="},{"when":{"$date":1470703225118},"hashedToken":"UoddY6vfFSD3aokkA8xB13nNSrf6BzyuqIctaDQNyx0="},{"when":{"$date":1470719060554},"hashedToken":"TB+4t1YMEnOjGnIlhDi32HWCEOuxICTg+gj8HFVzAlY="},{"when":{"$date":1470768637237},"hashedToken":"RPV+yT0bGKFPIMfOqdDgi0ubLCTahELQyAhMpr7pugk="},{"when":{"$date":1470770809602},"hashedToken":"6XPHZzlLnAOr3/vLcMt3C/UYGqCSeVBAAQG0zLjH6i0="},{"when":{"$date":1470817306369},"hashedToken":"NJN2nxnqvgEGmFkpFYg+N9WDLk5fjWkm+CiQ11qK7Ns="},{"when":{"$date":1470854447324},"hashedToken":"Q3WGqAHzTs6iLj0xQ+dYW4+/znhAf1P7uEVnj2D83ww="},{"when":{"$date":1470856023879},"hashedToken":"0ta8kAgP2QsSp37TGGDDslC3hz1Oo18I1y8N14CVjw0="},{"when":{"$date":1470902056301},"hashedToken":"JLYxS0I1evbizF3+55yAckQWVOwqm+kIFcFCgaChX5I="},{"when":{"$date":1470909384680},"hashedToken":"vbePM8dupRkLS4ia2ioNJtV6GZpBoZJa72ukM1CwmH8="},{"when":{"$date":1470913503123},"hashedToken":"VU+RLc4zaK5VZYRVlFc1bax1SQfpJfJzckGWsxAn7sQ="},{"when":{"$date":1470915566883},"hashedToken":"AEw8l7JzT2yVqspnptknWMY6qZBe3CifHDmtp1Tsov4="}]}},"profile":{"tags":["Free Food","Greek Life","BBQ","Party","Date Event","Trips/Excursions","Social Sciences","Natural Sciences","Business, Marketing, and Management","Mechanical Engineering","Civil Engineering","Accounting and Finance","Networking and Careers","Workshops and Speakers","Academic Fraternities","Honors Societies","Basketball","Baseball/Softball","Swimming","Water Polo","Badminton","Boxing","Cricket","Dodgeball","Bowling","Fencing","Golf","Field Hockey","Football","Shooting/Archery","Gymnastics","Tennis","Snow Sports","Water Sports","Volleyball","Kickball","Surfing","Hiking/Camping","Climbing","Quidditch","Biking","Crew/Rowing","Sailing","Martial Arts","Equestrian","Fishing","Hockey","Hunting","Kite Sports","Track/Cross - Country","Lacrosse","Rafting/River Sports","Driving","Scuba Diving/Snorkeling","Weightlifting/Body Building","General Health","Music Composition and Production","Concerts and Festivals","A Capella","Comedy/ Improv","Theater","Film and Telivision","Creative Writing, and Journalism","Art History","Sculpture, Ceramics, Glass and Wood Work","Poetry and Spoken Word","Christianity","Jedaism","Islam","Meditation","Retreats","Services","Scripture, Study and Discussion","LGBTQ and PRIDE","Multicultural","Democrats","Republicans","Independent, Green or Other","Student Government","Health and Medical Awareness","Ending Poverty","Ending Starvation and Hunger","Environmental Sciences","Education Reform","Judicial Reform","International politics"],"excludedClubs":[],"school":"Santa Clara University","phoneNumber":"+14153019886"}}
