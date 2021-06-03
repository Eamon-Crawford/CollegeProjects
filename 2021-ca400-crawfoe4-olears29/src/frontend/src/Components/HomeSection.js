import React from 'react'
import './HomeSection.css'

function HomeSection({lightBg, topLine, lightText, lightTextDesc, headline, description}) {
    return (
        <>
            <div className={lightBg ? 'home__home-section': 'home__home-section darkBg'}>
                <div className='container'>
                    <div className='row gome__home-row' style={{display: 'flex'}}>
                        <div className="col">
                            <div className='home__home-text-wrapper'>
                                <div className='top-line'>{topLine}</div>
                                <h1 className={lightText ? 'heading' : 'heading dark'}>{headline}</h1>
                                <p className={lightTextDesc ? 'home__home-subtitle' : 'home__home-subtitle dark'}>
                                    {description}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </>
    )
}

export default HomeSection;